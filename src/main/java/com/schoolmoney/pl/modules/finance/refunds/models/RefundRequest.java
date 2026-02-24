package com.schoolmoney.pl.modules.finance.refunds.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record RefundRequest(
        @NotNull(message = "Student ID is required")
        UUID studentId,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        Long amount,

        String reason
) {
}
