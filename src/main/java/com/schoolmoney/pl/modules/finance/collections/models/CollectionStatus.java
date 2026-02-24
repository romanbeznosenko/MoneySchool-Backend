package com.schoolmoney.pl.modules.finance.collections.models;

public enum CollectionStatus {
    ACTIVE,         // Accepting payments
    GOAL_REACHED,   // Goal reached, no more payments
    CLOSED,         // Closed by treasurer (auto-refund if not fully collected)
    ARCHIVED        // Manually archived by treasurer
}
