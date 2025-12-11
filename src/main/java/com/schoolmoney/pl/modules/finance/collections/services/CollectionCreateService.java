package com.schoolmoney.pl.modules.finance.collections.services;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.classes.management.ClassManager;
import com.schoolmoney.pl.modules.classes.management.ClassMapper;
import com.schoolmoney.pl.modules.classes.management.ClassNotFoundException;
import com.schoolmoney.pl.modules.classes.management.ClassTreasurerMismatchException;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import com.schoolmoney.pl.modules.classes.models.Class;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionManager;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionMapper;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionDAO;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionRequest;
import com.schoolmoney.pl.modules.finance.collections.models.Collection;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountManager;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountMapper;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccount;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountType;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollectionCreateService {
    private final HttpServletRequest request;
    private final CollectionManager collectionManager;
    private final CollectionMapper collectionMapper;
    private final ClassManager classManager;
    private final ClassMapper classMapper;
    private final FinanceAccountManager financeAccountManager;
    private final FinanceAccountMapper financeAccountMapper;

    private static final String COUNTRY_CODE = "PL";

    @Transactional
    public void create(CollectionRequest collectionRequest, UUID classId){
        log.info("Creating collection for class with id: {}", classId);

        UserDAO userDAO = (UserDAO)request.getAttribute("user");
        ClassDAO classDAO = classManager.findById(classId)
                .orElseThrow(ClassNotFoundException::new);
        if (!classDAO.getTreasurer().equals(userDAO)) {
            throw new ClassTreasurerMismatchException();
        }
        Class aClass = classMapper.mapToDomain(classDAO, new CycleAvoidingMappingContext());

        FinanceAccountDAO financeAccountDAO = FinanceAccountDAO.builder()
                .IBAN(generatePolishIban())
                .balance(0.0)
                .accountType(FinanceAccountType.COLLECTION)
                .isTreasurerAccount(true)
                .owner(null)
                .isArchived(false)
                .build();

        FinanceAccountDAO savedFinanceAccountDAO = financeAccountManager.saveToDatabase(financeAccountDAO);
        FinanceAccount financeAccount = financeAccountMapper.mapToDomain(
                savedFinanceAccountDAO,
                new CycleAvoidingMappingContext()
        );

        Collection collection = CollectionBuilders.buildCollectionFromRequest(
                collectionRequest,
                aClass,
                financeAccount
        );
        CollectionDAO collectionDAO = collectionMapper.mapToEntity(collection, new CycleAvoidingMappingContext());
        collectionDAO.setFinanceAccount(financeAccountDAO); // transient
        collectionManager.saveToDatabase(collectionDAO);    // cascade persist will handle financeAccountDAO

        log.info("Successfully created collection for class with id: {}", classId);
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
        for (char ch : COUNTRY_CODE.toCharArray()) {
            sb.append((int) ch - 55);
        }
        return sb.toString();
    }

    private int mod97(String numericIban) {
        BigInteger ibanNumber = new BigInteger(numericIban);
        return ibanNumber.mod(BigInteger.valueOf(97)).intValue();
    }
}
