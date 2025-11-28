package com.schoolmoney.pl.core.accountActivation.management;

public class AccountActivationTokenNotFoundException extends Exception {
    public AccountActivationTokenNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}