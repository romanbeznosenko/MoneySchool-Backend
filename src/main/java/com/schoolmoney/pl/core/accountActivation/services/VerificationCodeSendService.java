package com.schoolmoney.pl.core.accountActivation.services;//package com.easyjob.easyjobapi.core.accountActivation.services;

import com.mailgun.exception.MailGunException;
import com.schoolmoney.pl.core.accountActivation.management.VerificationCodeManager;
import com.schoolmoney.pl.core.accountActivation.models.VerificationCodeDAO;
import com.schoolmoney.pl.core.authAccount.management.AuthAccountManager;
import com.schoolmoney.pl.core.authAccount.management.AuthAccountMapper;
import com.schoolmoney.pl.core.authAccount.models.AuthAccountDAO;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import com.schoolmoney.pl.utils.mailer.ActivationEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationCodeSendService {
    private final VerificationCodeCreateService verificationCodeCreateService;
    private final VerificationCodeManager verificationCodeManager;
    private final ActivationEmailService activationEmailService;
    private final AuthAccountManager authAccountManager;
    private final AuthAccountMapper authAccountMapper;

    public void sendVerificationCode(AuthAccountDAO authAccountDAO) throws IOException {
        log.info("Sending account activation code to user: {}", authAccountDAO.getEmail());

        VerificationCodeDAO verificationCodeDAO = verificationCodeCreateService.generateVerificationCode(
                authAccountMapper.mapToDomain(authAccountDAO, new CycleAvoidingMappingContext()));
        try {
            activationEmailService.sendActivationEmail(authAccountDAO, verificationCodeDAO.getCode());
        } catch (MailGunException | IOException e) {
            verificationCodeManager.deleteVerificationCode(verificationCodeDAO);
            authAccountManager.deleteAuthAccount(authAccountDAO);
            throw e;
        }

        log.info("Sent account activation code to user: {}", authAccountDAO.getEmail());
    }
}

