package com.schoolmoney.pl.modules.finance.contributions.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ContributionCreateRequest(
        @NotNull
        @Schema(description = "Student ID for whom the payment is made", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID studentId,

        @NotNull
        @Positive
        @Schema(description = "Payment amount in PLN", example = "50.00")
        Double amount,

        @Size(max = 500)
        @Schema(description = "Optional note for the contribution", example = "Payment for school trip")
        String note
) {
}
