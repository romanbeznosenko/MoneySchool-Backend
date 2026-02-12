package com.schoolmoney.pl.modules.finance.refunds.services;

import com.schoolmoney.pl.core.student.management.StudentManager;
import com.schoolmoney.pl.core.student.management.StudentNotFoundException;
import com.schoolmoney.pl.core.student.models.StudentDAO;
import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionManager;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionNotFoundException;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountManager;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountNotFoundException;
import com.schoolmoney.pl.modules.finance.financeAccount.management.NotTreasurerException;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import com.schoolmoney.pl.modules.finance.refunds.management.RefundAmountExceedsCollectedException;
import com.schoolmoney.pl.modules.finance.refunds.management.RefundRepository;
import com.schoolmoney.pl.modules.finance.refunds.models.RefundDAO;
import com.schoolmoney.pl.modules.finance.refunds.models.RefundRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefundService {
    private final HttpServletRequest request;
    private final CollectionManager collectionManager;
    private final FinanceAccountManager financeAccountManager;
    private final StudentManager studentManager;
    private final RefundRepository refundRepository;

    @Transactional
    public void processRefund(UUID collectionId, RefundRequest refundRequest) {
        log.info("Processing refund for collection: {}", collectionId);
        UserDAO user = (UserDAO) request.getAttribute("user");

        CollectionDAO collection = collectionManager.findById(collectionId)
                .orElseThrow(CollectionNotFoundException::new);

        if (collection.getAClass().getTreasurer() == null ||
                !collection.getAClass().getTreasurer().getId().equals(user.getId())) {
            throw new NotTreasurerException();
        }

        StudentDAO student = studentManager.findById(refundRequest.studentId())
                .orElseThrow(StudentNotFoundException::new);

        FinanceAccountDAO collectionAccount = collection.getFinanceAccount();
        if (collectionAccount == null) {
            throw new FinanceAccountNotFoundException();
        }

        Double currentBalance = collectionAccount.getBalance();
        if (currentBalance == null || currentBalance < refundRequest.amount()) {
            throw new RefundAmountExceedsCollectedException();
        }

        FinanceAccountDAO parentAccount = financeAccountManager.findByOwnerId(student.getParent().getId())
                .orElseThrow(FinanceAccountNotFoundException::new);

        collectionAccount.setBalance(currentBalance - refundRequest.amount());
        parentAccount.setBalance(parentAccount.getBalance() + refundRequest.amount());

        financeAccountManager.saveToDatabase(collectionAccount);
        financeAccountManager.saveToDatabase(parentAccount);

        RefundDAO refund = RefundDAO.builder()
                .collection(collection)
                .student(student)
                .amount(refundRequest.amount())
                .reason(refundRequest.reason())
                .build();

        refundRepository.save(refund);

        log.info("Refund processed. Collection: {}, Student: {}, Amount: {}, Reason: {}",
                collectionId, refundRequest.studentId(), refundRequest.amount(), refundRequest.reason());
    }
}
