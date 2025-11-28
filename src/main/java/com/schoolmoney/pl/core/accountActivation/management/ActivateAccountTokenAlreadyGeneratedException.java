package com.schoolmoney.pl.core.accountActivation.management;

public class ActivateAccountTokenAlreadyGeneratedException extends Exception {
    public ActivateAccountTokenAlreadyGeneratedException(String errorMessage) {
        super(errorMessage);
    }
}