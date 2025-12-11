package com.schoolmoney.pl.modules.finance.contributions.models;

public enum ContributionStatus {
    PENDING,      // Payment initiated but not processed
    COMPLETED,    // Successfully processed
    FAILED,       // Payment failed
    REFUNDED      // Payment was refunded
}
