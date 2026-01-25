package com.schoolmoney.pl.modules.finance.financeAccount.service;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountManager;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountNotFoundException;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountOwnerMismatchException;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountEditRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FinanceAccountEditService {
    private final HttpServletRequest request;
    private final FinanceAccountManager financeAccountManager;

    public void editFinanceAccount(FinanceAccountEditRequest financeAccountEditRequest, UUID financeAccountId) {
        log.info("Top-up finance account");
        UserDAO user = (UserDAO)request.getAttribute("user");

        FinanceAccountDAO financeAccountDAO = financeAccountManager.findById(financeAccountId)
                .orElseThrow(FinanceAccountNotFoundException::new);

        if (!financeAccountDAO.getOwner().equals(user)) {
            throw new FinanceAccountOwnerMismatchException();
        }

        if (financeAccountEditRequest.balance() <= 0) {
            throw new IllegalArgumentException("Top-up amount must be positive");
        }

        Long currentBalance = financeAccountDAO.getBalance();
        Long newBalance = currentBalance + financeAccountEditRequest.balance();
        financeAccountDAO.setBalance(newBalance);

        financeAccountManager.saveToDatabase(financeAccountDAO);
        log.info("Account topped up. Previous balance: {}, Added: {}, New balance: {}",
                currentBalance, financeAccountEditRequest.balance(), newBalance);
    }
}
