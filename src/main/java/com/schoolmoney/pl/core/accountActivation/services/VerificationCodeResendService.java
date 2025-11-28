package com.schoolmoney.pl.core.accountActivation.services;//package com.easyjob.easyjobapi.core.accountActivation.services;

import com.schoolmoney.pl.core.accountActivation.management.AccountAlreadyActivatedException;
import com.schoolmoney.pl.core.accountActivation.management.ActivateAccountTokenAlreadyGeneratedException;
import com.schoolmoney.pl.core.accountActivation.management.VerificationCodeManager;
import com.schoolmoney.pl.core.accountActivation.management.VerificationCodeMapper;
import com.schoolmoney.pl.core.accountActivation.models.VerificationCode;
import com.schoolmoney.pl.core.accountActivation.models.VerificationCodeDAO;
import com.schoolmoney.pl.core.accountActivation.models.VerificationCodeResendRequest;
import com.schoolmoney.pl.core.authAccount.management.AuthAccountManager;
import com.schoolmoney.pl.core.authAccount.management.AuthAccountMapper;
import com.schoolmoney.pl.core.authAccount.models.AuthAccount;
import com.schoolmoney.pl.core.authAccount.models.AuthAccountDAO;
import com.schoolmoney.pl.core.user.management.UserNotFoundException;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import com.schoolmoney.pl.utils.mailer.ActivationEmailService;
import com.mailgun.exception.MailGunException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationCodeResendService {
    private final AuthAccountManager authAccountManager;
    private final AuthAccountMapper authAccountMapper;
    private final VerificationCodeManager verificationCodeManager;
    private final VerificationCodeMapper verificationCodeMapper;
    private final VerificationCodeCreateService verificationCodeCreateService;
    private final ActivationEmailService activationEmailService;

    public void resendVerificationCode(VerificationCodeResendRequest request) throws UserNotFoundException, AccountAlreadyActivatedException, ActivateAccountTokenAlreadyGeneratedException {
        log.info("Resending account activation code to user: {}", request.email());

        String trimmedEmail = request.email()
                                     .strip();

        AuthAccountDAO authAccountDAO = authAccountManager.findByEmail(trimmedEmail)
                                                          .orElseThrow(
                                                                  () -> new UserNotFoundException("User not found!"));
        AuthAccount authAccount = authAccountMapper.mapToDomain(authAccountDAO, new CycleAvoidingMappingContext());
        if (Boolean.TRUE.equals(authAccount.getIsActivated()))
            throw new AccountAlreadyActivatedException("User is already activated!");

        VerificationCodeDAO verificationCodeDAO = verificationCodeManager.findVerificationCodeByUser(authAccountDAO)
                                                                         .orElse(null);
        VerificationCode verificationCode = verificationCodeMapper.mapToDomain(verificationCodeDAO,
                                                                               new CycleAvoidingMappingContext());
        Instant now = Instant.now();
        Instant createdAt = verificationCode.getCreatedAt();

        long duration = Duration.between(createdAt, now).toMinutes();

        if (duration < 3)
            throw new ActivateAccountTokenAlreadyGeneratedException(
                    "Account activation token has been generated less than 3 minutes ago! Try again in " + (3 - duration) + " minutes");
        if (verificationCodeDAO != null) {
            verificationCodeManager.deleteVerificationCode(verificationCodeDAO);
        }

        verificationCodeDAO = verificationCodeCreateService.generateVerificationCode(authAccount);

        try {
            activationEmailService.sendActivationEmail(authAccountDAO, verificationCodeDAO.getCode());
        } catch (MailGunException e) {
            verificationCodeManager.deleteVerificationCode(verificationCodeDAO);
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Resent account activation code to user: {}", request.email());
    }
}

