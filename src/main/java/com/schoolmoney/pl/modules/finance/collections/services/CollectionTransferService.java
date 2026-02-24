package com.schoolmoney.pl.modules.finance.collections.services;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.classes.management.ClassManager;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionManager;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionNotFoundException;
import com.schoolmoney.pl.modules.finance.collections.management.NoFundsToTransferException;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountManager;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountNotFoundException;
import com.schoolmoney.pl.modules.finance.financeAccount.management.NotTreasurerException;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollectionTransferService {
    private final HttpServletRequest request;
    private final CollectionManager collectionManager;
    private final FinanceAccountManager financeAccountManager;
    private final ClassManager classManager;

    @Transactional
    public void transferToTreasurer(UUID collectionId) {
        log.info("Transferring collection funds to treasurer");
        UserDAO user = (UserDAO) request.getAttribute("user");

        // Get collection
        CollectionDAO collection = collectionManager.findById(collectionId)
                .orElseThrow(CollectionNotFoundException::new);

        // Get class and check if user is treasurer
        ClassDAO aClass = collection.getAClass();
        if (aClass.getTreasurer() == null || !aClass.getTreasurer().getId().equals(user.getId())) {
            throw new NotTreasurerException();
        }

        // Get collection's finance account
        FinanceAccountDAO collectionAccount = collection.getFinanceAccount();
        if (collectionAccount == null) {
            throw new FinanceAccountNotFoundException();
        }

        // Check if there are funds to transfer
        Double collectedAmount = collectionAccount.getBalance();
        if (collectedAmount == null || collectedAmount <= 0) {
            throw new NoFundsToTransferException();
        }

        // Get treasurer's finance account
        FinanceAccountDAO treasurerAccount = financeAccountManager.findByOwnerId(user.getId())
                .orElseThrow(FinanceAccountNotFoundException::new);

        // Perform atomic transfer
        collectionAccount.setBalance(0.0);
        double currentTreasurerBalance = treasurerAccount.getBalance() != null ? treasurerAccount.getBalance() : 0.0;
        treasurerAccount.setBalance(currentTreasurerBalance + collectedAmount);

        financeAccountManager.saveToDatabase(collectionAccount);
        financeAccountManager.saveToDatabase(treasurerAccount);

        log.info("Transfer completed. Amount: {}, From collection: {}, To treasurer: {}",
                collectedAmount, collectionId, user.getId());
    }
}
