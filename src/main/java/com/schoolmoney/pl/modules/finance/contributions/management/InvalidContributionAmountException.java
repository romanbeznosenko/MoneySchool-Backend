package com.schoolmoney.pl.modules.finance.contributions.management;

public class InvalidContributionAmountException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Invalid contribution amount";

    public InvalidContributionAmountException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidContributionAmountException(String message) {
        super(message);
    }
}
