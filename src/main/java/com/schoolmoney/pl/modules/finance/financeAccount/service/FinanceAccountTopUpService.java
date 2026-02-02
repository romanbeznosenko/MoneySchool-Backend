package com.schoolmoney.pl.modules.finance.financeAccount.service;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountManager;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountNotFoundException;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountOwnerMismatchException;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountTopUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FinanceAccountTopUpService {
    private final HttpServletRequest request;
    private final FinanceAccountManager financeAccountManager;

    @Transactional
    public void topUpFinanceAccount(FinanceAccountTopUpRequest topUpRequest, UUID financeAccountId) {
        log.info("Topping up finance account");
        UserDAO user = (UserDAO) request.getAttribute("user");

        FinanceAccountDAO financeAccountDAO = financeAccountManager.findById(financeAccountId)
                .orElseThrow(FinanceAccountNotFoundException::new);

        if (!financeAccountDAO.getOwner().equals(user)) {
            throw new FinanceAccountOwnerMismatchException();
        }

        Double current = financeAccountDAO.getBalance() == null ? 0.0 : financeAccountDAO.getBalance();
        Double amount = topUpRequest.amount();

        financeAccountDAO.setBalance(current + amount);
        financeAccountManager.saveToDatabase(financeAccountDAO);
    }
}