package com.schoolmoney.pl.modules.finance.financeAccount.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ExternalPaymentRequest(
        @NotBlank(message = "Recipient is required")
        String recipient,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        Long amount,

        @NotBlank(message = "Description is required")
        String description
) {
}
