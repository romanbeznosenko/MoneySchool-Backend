package com.schoolmoney.pl.modules.finance.collections.services;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionManager;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionNotFoundException;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionDAO;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionStatus;
import com.schoolmoney.pl.modules.finance.contributions.management.ContributionManager;
import com.schoolmoney.pl.modules.finance.contributions.models.ContributionDAO;
import com.schoolmoney.pl.modules.finance.contributions.models.ContributionStatus;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountManager;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountNotFoundException;
import com.schoolmoney.pl.modules.finance.financeAccount.management.NotTreasurerException;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import com.schoolmoney.pl.modules.finance.refunds.management.RefundRepository;
import com.schoolmoney.pl.modules.finance.refunds.models.RefundDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollectionCloseService {
    private final HttpServletRequest request;
    private final CollectionManager collectionManager;
    private final ContributionManager contributionManager;
    private final FinanceAccountManager financeAccountManager;
    private final RefundRepository refundRepository;

    @Transactional
    public void closeCollection(UUID collectionId) {
        log.info("Closing collection with id: {}", collectionId);

        UserDAO user = (UserDAO) request.getAttribute("user");

        CollectionDAO collection = collectionManager.findById(collectionId)
                .orElseThrow(CollectionNotFoundException::new);

        // Verify user is treasurer
        if (collection.getAClass().getTreasurer() == null ||
                !collection.getAClass().getTreasurer().getId().equals(user.getId())) {
            throw new NotTreasurerException();
        }

        FinanceAccountDAO collectionAccount = collection.getFinanceAccount();
        if (collectionAccount == null) {
            throw new FinanceAccountNotFoundException();
        }

        // Auto-refund all payers if collection has balance
        Double balance = collectionAccount.getBalance();
        if (balance != null && balance > 0) {
            autoRefundAllPayers(collection, collectionAccount);
        }

        // Update collection status to CLOSED
        collection.setStatus(CollectionStatus.CLOSED);
        collection.setClosedAt(Instant.now());
        collection.setIsArchived(true);
        collection.setArchivedAt(Instant.now());
        collectionManager.saveToDatabase(collection);

        log.info("Collection {} closed with auto-refund completed", collectionId);
    }

    private void autoRefundAllPayers(CollectionDAO collection, FinanceAccountDAO collectionAccount) {
        // Get sum of contributions grouped by payer
        List<Object[]> payerSums = contributionManager.sumByCollectionGroupedByPayer(
                collection.getId(),
                ContributionStatus.COMPLETED
        );

        // Load all completed contributions once, outside the loop
        List<ContributionDAO> allContributions = contributionManager.findByCollectionIdAndStatus(
                collection.getId(), ContributionStatus.COMPLETED
        );

        for (Object[] payerSum : payerSums) {
            UUID payerId = (UUID) payerSum[0];
            double amount = ((Number) payerSum[1]).doubleValue();

            if (amount <= 0) continue;

            // Find payer's finance account
            FinanceAccountDAO payerAccount = financeAccountManager.findByOwnerId(payerId)
                    .orElse(null);

            if (payerAccount == null) {
                log.warn("Could not find finance account for payer: {}, skipping refund of {} PLN",
                        payerId, amount);
                continue;
            }

            // Check if collection account has enough balance
            double currentBalance = collectionAccount.getBalance() != null ? collectionAccount.getBalance() : 0.0;
            double refundAmount = Math.min(amount, currentBalance);

            if (refundAmount <= 0) {
                log.warn("No more funds in collection account to refund payer: {}", payerId);
                break;
            }

            // Process refund
            collectionAccount.setBalance(currentBalance - refundAmount);
            double payerBalance = payerAccount.getBalance() != null ? payerAccount.getBalance() : 0.0;
            payerAccount.setBalance(payerBalance + refundAmount);

            financeAccountManager.saveToDatabase(collectionAccount);
            financeAccountManager.saveToDatabase(payerAccount);

            // Find first contribution for this payer from preloaded list
            allContributions.stream()
                    .filter(c -> c.getPayer().getId().equals(payerId))
                    .findFirst()
                    .ifPresent(payerContribution -> {
                        RefundDAO refund = RefundDAO.builder()
                                .collection(collection)
                                .student(payerContribution.getStudent())
                                .amount(Math.round(refundAmount))
                                .reason("Auto-refund: Collection closed by treasurer")
                                .build();
                        refundRepository.save(refund);
                    });

            log.info("Auto-refund: {} PLN returned to payer {}", refundAmount, payerId);
        }
    }
}
