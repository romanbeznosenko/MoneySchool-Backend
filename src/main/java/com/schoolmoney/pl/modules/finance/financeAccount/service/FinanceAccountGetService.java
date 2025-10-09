package com.schoolmoney.pl.modules.finance.financeAccount.service;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountManager;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountNotFoundException;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountGetResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FinanceAccountGetService {
    private final FinanceAccountManager financeAccountManager;
    private final HttpServletRequest request;

    public FinanceAccountGetResponse getUserFinanceAccount(){
        log.info("Getting user finance account");
        UserDAO user = (UserDAO)request.getAttribute("user");

        FinanceAccountDAO financeAccountDAO = financeAccountManager.findByOwnerId(user.getId())
                .orElseThrow(FinanceAccountNotFoundException::new);

        return FinanceAccountGetResponse.builder()
                .id(financeAccountDAO.getId())
                .IBAN(financeAccountDAO.getIBAN())
                .balance(financeAccountDAO.getBalance())
                .isTreasurerAccount(financeAccountDAO.getIsTreasurerAccount())
                .build();
    }
}
