package com.schoolmoney.pl.modules.finance.financeAccount.service;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.classes.management.ClassManager;
import com.schoolmoney.pl.modules.classes.management.ClassSpecifications;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountAlreadyExistingException;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountForbiddenCreationException;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountManager;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountCreateRequest;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class FinanceAccountCreateService {
    private final HttpServletRequest request;
    private final FinanceAccountManager financeAccountManager;
    private final ClassManager classManager;

    private static final String COUNTRY_CODE = "PL";

    public void createFinanceAccount(FinanceAccountCreateRequest financeAccountCreateRequest) {
        log.info("Creating finance account");
        UserDAO user = (UserDAO)request.getAttribute("user");

        FinanceAccountDAO existingAccount = financeAccountManager.findByOwnerId(user.getId())
                .orElse(null);

        // Check if user already has a finance account
        if (existingAccount != null) {
            throw new FinanceAccountAlreadyExistingException();
        }

        Specification<ClassDAO> treasurerSpec = ClassSpecifications.findByUserTreasurer(user.getId());
        Page<ClassDAO> treasurerClasses = classManager.findUserTreasurerClasses(treasurerSpec, PageRequest.of(0, Integer.MAX_VALUE));

        boolean isActualTreasurer = !treasurerClasses.isEmpty();

        // If user wants to create treasurer account but is not a treasurer - forbid
        if (financeAccountCreateRequest.isTreasurerAccount() && !isActualTreasurer) {
            throw new FinanceAccountForbiddenCreationException();
        }

        // If user is a treasurer but wants to create regular account - forbid
        if (!financeAccountCreateRequest.isTreasurerAccount() && isActualTreasurer) {
            throw new FinanceAccountForbiddenCreationException();
        }

        FinanceAccountDAO newAccount = FinanceAccountDAO.builder()
                .IBAN(generatePolishIban())
                .balance(financeAccountCreateRequest.balance())
                .isTreasurerAccount(financeAccountCreateRequest.isTreasurerAccount())
                .accountType(financeAccountCreateRequest.isTreasurerAccount() ?
                        FinanceAccountType.COLLECTION : FinanceAccountType.USER)
                .owner(user)
                .build();

        // Save the account to database
        financeAccountManager.saveToDatabase(newAccount);
        log.info("Finance account created successfully with IBAN: {}", newAccount.getIBAN());
    }

    private String generatePolishIban() {
        Random random = new Random();

        StringBuilder bban = new StringBuilder();
        for (int i = 0; i < 24; i++) {
            bban.append(random.nextInt(10));
        }

        String ibanRaw = bban + countryCodeToDigits() + "00";

        int checksum = 98 - mod97(ibanRaw);
        String checksumStr = String.format("%02d", checksum);

        return COUNTRY_CODE + checksumStr + bban;
    }

    private String countryCodeToDigits() {
        StringBuilder sb = new StringBuilder();
        for (char ch : FinanceAccountCreateService.COUNTRY_CODE.toCharArray()) {
            sb.append((int) ch - 55);
        }
        return sb.toString();
    }

    private int mod97(String numericIban) {
        BigInteger ibanNumber = new BigInteger(numericIban);
        return ibanNumber.mod(BigInteger.valueOf(97)).intValue();
    }
}
