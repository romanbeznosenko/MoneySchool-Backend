package com.schoolmoney.pl.core.accountActivation.management;

import com.schoolmoney.pl.core.accountActivation.models.VerificationCodeDAO;
import com.schoolmoney.pl.core.authAccount.models.AuthAccountDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationCodeManager {
    private final VerificationCodeRepository verificationCodeRepository;

    public VerificationCodeDAO saveVerificationCodeToDatabase(VerificationCodeDAO verificationCodeDAO) {
        return verificationCodeRepository.save(verificationCodeDAO);
    }

    public Optional<VerificationCodeDAO> findVerificationCodeById(UUID id) {
        return verificationCodeRepository.findById(id);
    }

    public List<VerificationCodeDAO> findAllVerificationCode() {
        return verificationCodeRepository.findAll();
    }

    public void deleteVerificationCode(VerificationCodeDAO verificationCodeDAO) {
        verificationCodeRepository.delete(verificationCodeDAO);
    }

    public Optional<VerificationCodeDAO> findVerificationCodeByCode(String code) {
        return verificationCodeRepository.findByCode(code);
    }

    public Optional<VerificationCodeDAO> findVerificationCodeByUser(AuthAccountDAO authAccountDAO) {
        return verificationCodeRepository.findByUser(authAccountDAO);
    }
}
