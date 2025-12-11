package com.schoolmoney.pl.modules.finance.contributions.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ContributionSimpleResponse(
        @Schema(description = "Contribution ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID contributionId,

        @Schema(description = "Payer information", implementation = UserSimpleResponse.class)
        UserSimpleResponse payer,

        @Schema(description = "Payment amount in PLN", example = "50.00")
        Double amount,

        @Schema(description = "When contribution was created")
        Instant createdAt
) {
}
