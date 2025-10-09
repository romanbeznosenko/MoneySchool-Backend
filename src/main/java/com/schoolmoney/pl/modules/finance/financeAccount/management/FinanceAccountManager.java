package com.schoolmoney.pl.modules.finance.financeAccount.management;

import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinanceAccountManager {
    private final FinanceAccountRepository financeAccountRepository;

    public FinanceAccountDAO saveToDatabase(FinanceAccountDAO financeAccount) {
        return financeAccountRepository.save(financeAccount);
    }

    public Optional<FinanceAccountDAO> findByOwnerId(UUID id) {
        return  financeAccountRepository.findByOwnerId(id);
    }

    public Optional<FinanceAccountDAO> findById(UUID id) {
        return  financeAccountRepository.findById(id);
    }
}