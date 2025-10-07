package com.schoolmoney.pl.core.login.management;

public class AccountBlockedException extends Exception {
    public AccountBlockedException(String errorMessage) {
        super(errorMessage);
    }
}