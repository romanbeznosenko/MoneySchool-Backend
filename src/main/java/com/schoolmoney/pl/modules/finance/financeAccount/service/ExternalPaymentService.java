package com.schoolmoney.pl.modules.finance.financeAccount.service;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountManager;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountNotFoundException;
import com.schoolmoney.pl.modules.finance.financeAccount.management.InsufficientFundsException;
import com.schoolmoney.pl.modules.finance.financeAccount.management.NotTreasurerException;
import com.schoolmoney.pl.modules.finance.financeAccount.models.ExternalPaymentRequest;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalPaymentService {
    private final HttpServletRequest request;
    private final FinanceAccountManager financeAccountManager;

    @Transactional
    public void processExternalPayment(ExternalPaymentRequest paymentRequest) {
        log.info("Processing external payment");
        UserDAO user = (UserDAO) request.getAttribute("user");

        FinanceAccountDAO financeAccountDAO = financeAccountManager.findByOwnerId(user.getId())
                .orElseThrow(FinanceAccountNotFoundException::new);

        if (!financeAccountDAO.getIsTreasurerAccount()) {
            throw new NotTreasurerException();
        }

        if (financeAccountDAO.getBalance() < paymentRequest.amount()) {
            throw new InsufficientFundsException();
        }

        Long newBalance = financeAccountDAO.getBalance() - paymentRequest.amount();
        financeAccountDAO.setBalance(newBalance);

        financeAccountManager.saveToDatabase(financeAccountDAO);

        log.info("External payment processed. Recipient: {}, Amount: {}, Description: {}, New balance: {}",
                paymentRequest.recipient(), paymentRequest.amount(), paymentRequest.description(), newBalance);
    }
}
