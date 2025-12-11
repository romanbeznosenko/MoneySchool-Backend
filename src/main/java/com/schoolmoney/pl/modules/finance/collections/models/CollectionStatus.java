package com.schoolmoney.pl.modules.finance.collections.models;

public enum CollectionStatus {
    ACTIVE,         // Accepting payments
    GOAL_REACHED,   // Goal reached, no more payments
    ARCHIVED        // Manually archived by treasurer
}
