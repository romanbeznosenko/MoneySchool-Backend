package com.schoolmoney.pl.core.accountActivation.services;

import com.schoolmoney.pl.core.accountActivation.management.VerificationCodeManager;
import com.schoolmoney.pl.core.accountActivation.management.VerificationCodeMapper;
import com.schoolmoney.pl.core.accountActivation.models.VerificationCode;
import com.schoolmoney.pl.core.accountActivation.models.VerificationCodeDAO;
import com.schoolmoney.pl.core.authAccount.models.AuthAccount;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationCodeCreateService {
    private final VerificationCodeManager verificationCodeManager;
    private final VerificationCodeMapper verificationCodeMapper;

    public VerificationCodeDAO generateVerificationCode(AuthAccount user) {
        log.info("Creating verification code for user: {}", user.getEmail());
        Random rand = new Random();
        String code = String.format("%04d", rand.nextInt(10000));
        Instant verificationCodeExpireAt = Instant.now()
                                                  .plus(24 * 60, ChronoUnit.MINUTES);
        VerificationCode verificationCode = VerificationCodeBuilders.buildVerificationCode(code,
                                                                                           verificationCodeExpireAt,
                                                                                           user);
        VerificationCodeDAO verificationCodeDAO = verificationCodeMapper.mapToEntity(verificationCode,
                                                                                     new CycleAvoidingMappingContext());
        log.info("Created verification code for user: {}", user.getEmail());
        return verificationCodeManager.saveVerificationCodeToDatabase(verificationCodeDAO);
    }
}
