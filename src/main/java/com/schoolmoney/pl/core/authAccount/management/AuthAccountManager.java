package com.schoolmoney.pl.core.authAccount.management;

import com.schoolmoney.pl.core.authAccount.models.AuthAccountDAO;
import com.schoolmoney.pl.utils.enums.AuthTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthAccountManager {
    private final AuthAccountRepository authAccountRepository;

    public AuthAccountDAO saveToDatabase(AuthAccountDAO authAccountDAO) {
        return authAccountRepository.save(authAccountDAO);
    }

    public void deleteAuthAccount(AuthAccountDAO authAccountDAO) {
        authAccountRepository.delete(authAccountDAO);
    }

    public Optional<AuthAccountDAO> findByEmail(String email) {
        return authAccountRepository.findByEmail(email);
    }


    public Optional<AuthAccountDAO> findByUserId(UUID id) {
        return authAccountRepository.findByUserId(id);
    }

    public Optional<AuthAccountDAO> findByEmailAndAuthTypeAndIsActivatedTrue(String email, AuthTypeEnum authType) {
        return authAccountRepository.findByEmailAndAuthType(email, authType);
    }
}
