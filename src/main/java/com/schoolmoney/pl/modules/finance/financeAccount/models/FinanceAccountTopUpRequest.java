package com.schoolmoney.pl.modules.finance.financeAccount.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record FinanceAccountTopUpRequest(
        @Schema(description = "Top-up amount to add to the finance account balance", example = "50.0")
        @NotNull
        @Positive
        Double amount
) {
}