package com.schoolmoney.pl.modules.finance.contributions.management;

public class ContributionNotFoundException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Contribution not found";

    public ContributionNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public ContributionNotFoundException(String message) {
        super(message);
    }
}
