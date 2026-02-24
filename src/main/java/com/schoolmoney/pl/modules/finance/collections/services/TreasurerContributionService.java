package com.schoolmoney.pl.modules.finance.collections.services;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionManager;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionNotFoundException;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionDAO;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionStatus;
import com.schoolmoney.pl.modules.finance.contributions.management.ContributionExceedsLimitException;
import com.schoolmoney.pl.modules.finance.contributions.management.InsufficientBalanceException;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountManager;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountNotFoundException;
import com.schoolmoney.pl.modules.finance.financeAccount.management.NotTreasurerException;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TreasurerContributionService {
    private final HttpServletRequest request;
    private final CollectionManager collectionManager;
    private final FinanceAccountManager financeAccountManager;

    @Transactional
    public void treasurerContribute(UUID collectionId, Double amount, String note) {
        log.info("Treasurer contributing {} PLN to collection: {}", amount, collectionId);

        UserDAO user = (UserDAO) request.getAttribute("user");

        CollectionDAO collection = collectionManager.findById(collectionId)
                .orElseThrow(CollectionNotFoundException::new);

        // Verify user is treasurer
        if (collection.getAClass().getTreasurer() == null ||
                !collection.getAClass().getTreasurer().getId().equals(user.getId())) {
            throw new NotTreasurerException();
        }

        // Verify collection is active
        if (collection.getStatus() != CollectionStatus.ACTIVE) {
            throw new ContributionExceedsLimitException("Collection is not active");
        }

        if (amount == null || amount <= 0) {
            throw new ContributionExceedsLimitException("Amount must be positive");
        }

        // Get treasurer's finance account
        FinanceAccountDAO treasurerAccount = financeAccountManager.findByOwnerId(user.getId())
                .orElseThrow(() -> new FinanceAccountNotFoundException("Treasurer must have a finance account"));

        double treasurerBalance = treasurerAccount.getBalance() != null ? treasurerAccount.getBalance() : 0.0;
        if (treasurerBalance < amount) {
            throw new InsufficientBalanceException(
                    String.format("Insufficient balance. Available: %.2f PLN", treasurerBalance)
            );
        }

        // Get collection's finance account
        FinanceAccountDAO collectionAccount = collection.getFinanceAccount();
        if (collectionAccount == null) {
            throw new FinanceAccountNotFoundException();
        }

        // Process transfer
        double collectionBalance = collectionAccount.getBalance() != null ? collectionAccount.getBalance() : 0.0;
        treasurerAccount.setBalance(treasurerBalance - amount);
        collectionAccount.setBalance(collectionBalance + amount);

        financeAccountManager.saveToDatabase(treasurerAccount);
        financeAccountManager.saveToDatabase(collectionAccount);

        // Check if goal reached
        if (collectionAccount.getBalance() >= collection.getGoal()) {
            collection.setStatus(CollectionStatus.GOAL_REACHED);
            collection.setGoalReachedAt(Instant.now());
            collectionManager.saveToDatabase(collection);
        }

        log.info("Treasurer contribution completed: {} PLN to collection {}", amount, collectionId);
    }
}
