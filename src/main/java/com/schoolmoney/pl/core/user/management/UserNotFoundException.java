package com.schoolmoney.pl.core.user.management;

public class UserNotFoundException extends Exception {

    private static final String DEFAULT_MESSAGE = "User not found in security context";

    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}