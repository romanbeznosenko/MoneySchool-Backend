package com.schoolmoney.pl.core.accountActivation.services;

import com.schoolmoney.pl.core.accountActivation.management.*;
import com.schoolmoney.pl.core.accountActivation.models.AccountActivationRequest;
import com.schoolmoney.pl.core.accountActivation.models.VerificationCode;
import com.schoolmoney.pl.core.accountActivation.models.VerificationCodeDAO;
import com.schoolmoney.pl.core.accountActivation.models.VerificationCodeResendRequest;
import com.schoolmoney.pl.core.authAccount.management.AuthAccountManager;
import com.schoolmoney.pl.core.authAccount.management.AuthAccountMapper;
import com.schoolmoney.pl.core.authAccount.models.AuthAccount;
import com.schoolmoney.pl.core.authAccount.models.AuthAccountDAO;
import com.schoolmoney.pl.core.user.management.UserManager;
import com.schoolmoney.pl.core.user.management.UserNotFoundException;
import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountActivationService {
    private final VerificationCodeManager verificationCodeManager;
    private final VerificationCodeMapper verificationCodeMapper;
    private final VerificationCodeResendService verificationCodeResendService;
    private final AuthAccountMapper authAccountMapper;
    private final AuthAccountManager authAccountManager;
    private final UserManager userManager;

    public void activateUser(AccountActivationRequest request) throws UserNotFoundException, AccountAlreadyActivatedException, AccountActivationTokenNotFoundException, ActivateAccountTokenExpiredException, ActivateAccountTokenAlreadyGeneratedException {
        log.info("Activating user");
        VerificationCodeDAO verificationCodeDAO = verificationCodeManager.findVerificationCodeByCode(request.code())
                                                                         .orElseThrow(
                                                                                 () -> new AccountActivationTokenNotFoundException(
                                                                                         "Account activation token not found!"));
        VerificationCode verificationCode = verificationCodeMapper.mapToDomain(verificationCodeDAO,
                                                                               new CycleAvoidingMappingContext());
        if (Instant.now()
                   .isAfter(verificationCode.getVerificationCodeExpireAt())) {
            verificationCodeResendService.resendVerificationCode(VerificationCodeResendRequest.builder()
                                                                                              .email(verificationCode.getUser()
                                                                                                                     .getEmail())
                                                                                              .build());
            throw new ActivateAccountTokenExpiredException("Account activation token has expired!");
        }

        AuthAccount authAccount = verificationCode.getUser();

        authAccount.setIsActivated(true);

        verificationCodeManager.deleteVerificationCode(verificationCodeDAO);
        log.info("Activated user");

        log.info("Creating user account");
        UserDAO userDAO = userManager.findUserById(authAccount.getUserId())
                .orElseThrow(UserNotFoundException::new);
        userDAO.setIsArchived(false);
        userManager.saveToDatabase(userDAO);

        authAccount.setUserId(userDAO.getId());
        AuthAccountDAO authAccountDAO = authAccountMapper.mapToEntity(authAccount, new CycleAvoidingMappingContext());
        authAccountManager.saveToDatabase(authAccountDAO);

        log.info("Creating default organization");
    }
}

