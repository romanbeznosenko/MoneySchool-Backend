package com.schoolmoney.pl.core.accountActivation.management;

public class ActivateAccountTokenExpiredException extends Exception {
    public ActivateAccountTokenExpiredException(String errorMessage) {
        super(errorMessage);
    }
}