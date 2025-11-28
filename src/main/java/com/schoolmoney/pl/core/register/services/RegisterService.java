package com.schoolmoney.pl.core.register.services;

import com.schoolmoney.pl.core.accountActivation.services.VerificationCodeSendService;
import com.schoolmoney.pl.core.authAccount.management.AuthAccountManager;
import com.schoolmoney.pl.core.authAccount.management.AuthAccountMapper;
import com.schoolmoney.pl.core.authAccount.models.AuthAccount;
import com.schoolmoney.pl.core.authAccount.models.AuthAccountDAO;
import com.schoolmoney.pl.core.authAccount.services.AuthAccountBuilders;
import com.schoolmoney.pl.core.register.management.UserAlreadyExistException;
import com.schoolmoney.pl.core.register.models.RegisterRequest;
import com.schoolmoney.pl.core.user.management.UserManager;
import com.schoolmoney.pl.core.user.management.UserMapper;
import com.schoolmoney.pl.core.user.models.User;
import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.core.user.services.UserBuilders;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import com.schoolmoney.pl.utils.enums.AuthTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegisterService {
    private final PasswordEncoder passwordEncoder;
    private final AuthAccountManager authAccountManager;
    private final AuthAccountMapper authAccountMapper;
    private final UserMapper userMapper;
    private final UserManager userManager;
    private final VerificationCodeSendService verificationCodeSendService;

    public void registerUser(RegisterRequest request) throws UserAlreadyExistException, IOException {
        log.info("Creating new app user: {}", request.email());
        String trimmedEmail = request.email()
                                     .strip()
                                     .toLowerCase();
        String trimmedPassword = request.password()
                                        .strip();

        AuthAccountDAO authAccountDAO = authAccountManager.findByEmail(trimmedEmail)
                                                          .orElse(null);
        if (authAccountDAO != null && authAccountDAO.getIsActivated())
            throw new UserAlreadyExistException("User already exist!");

        if (authAccountDAO == null) {
            AuthAccount authAccount = AuthAccountBuilders.buildAuthAccount(trimmedEmail,
                                                                           passwordEncoder.encode(trimmedPassword),
                                                                           AuthTypeEnum.EMAIL);

            authAccountDAO = authAccountMapper.mapToEntity(authAccount, new CycleAvoidingMappingContext());

            User user = UserBuilders.buildUserFromEmail(authAccount.getEmail());
            UserDAO userDAO = userMapper.mapToEntity(user, new CycleAvoidingMappingContext());
            userDAO = userManager.saveToDatabase(userDAO);
            authAccountDAO.setUserId(userDAO.getId());
            authAccountManager.saveToDatabase(authAccountDAO);
        }

        verificationCodeSendService.sendVerificationCode(authAccountDAO);
        log.info("Created new app user: {}", request.email());
    }
}

