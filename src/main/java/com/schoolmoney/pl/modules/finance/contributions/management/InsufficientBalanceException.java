package com.schoolmoney.pl.modules.finance.contributions.management;

public class InsufficientBalanceException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Insufficient balance in account";

    public InsufficientBalanceException() {
        super(DEFAULT_MESSAGE);
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
