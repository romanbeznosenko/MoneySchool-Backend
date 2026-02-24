package com.schoolmoney.pl.modules.finance.collections.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TreasurerContributionRequest(
        @NotNull
        @Positive
        @Schema(description = "Amount to contribute", example = "100.00")
        Double amount,

        @Schema(description = "Optional note for the contribution")
        String note
) {
}
