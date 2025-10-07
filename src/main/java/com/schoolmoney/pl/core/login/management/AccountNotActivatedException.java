package com.schoolmoney.pl.core.login.management;

public class AccountNotActivatedException extends Exception {
    public AccountNotActivatedException(String errorMessage) {
        super(errorMessage);
    }
}