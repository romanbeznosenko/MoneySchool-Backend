package com.schoolmoney.pl.modules.finance.contributions.management;

public class ContributionExceedsLimitException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Contribution amount exceeds allowed limit";

    public ContributionExceedsLimitException() {
        super(DEFAULT_MESSAGE);
    }

    public ContributionExceedsLimitException(String message) {
        super(message);
    }
}
