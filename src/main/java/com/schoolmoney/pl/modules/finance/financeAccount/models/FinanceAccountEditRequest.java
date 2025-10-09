package com.schoolmoney.pl.modules.finance.financeAccount.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record FinanceAccountEditRequest(
        @Schema(description = "Finance account balance", example = "23.5")
        @NotNull
        @PositiveOrZero
        Double balance
) {
}
