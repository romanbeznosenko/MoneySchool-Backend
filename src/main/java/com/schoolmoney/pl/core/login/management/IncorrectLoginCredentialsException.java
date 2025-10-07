package com.schoolmoney.pl.core.login.management;

public class IncorrectLoginCredentialsException extends Exception {
    public IncorrectLoginCredentialsException(String errorMessage) {
        super(errorMessage);
    }
}