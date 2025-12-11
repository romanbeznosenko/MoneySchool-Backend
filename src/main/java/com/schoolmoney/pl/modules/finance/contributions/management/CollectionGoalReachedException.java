package com.schoolmoney.pl.modules.finance.contributions.management;

public class CollectionGoalReachedException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Collection goal has been reached, no more payments accepted";

    public CollectionGoalReachedException() {
        super(DEFAULT_MESSAGE);
    }

    public CollectionGoalReachedException(String message) {
        super(message);
    }
}
