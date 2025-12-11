package com.schoolmoney.pl.modules.finance.contributions.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ContributionResponse(
        @Schema(description = "Contribution ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID contributionId,

        @Schema(description = "Collection information", implementation = CollectionSimpleResponse.class)
        CollectionSimpleResponse collection,

        @Schema(description = "Student information", implementation = StudentSimpleResponse.class)
        StudentSimpleResponse student,

        @Schema(description = "Payer information", implementation = UserSimpleResponse.class)
        UserSimpleResponse payer,

        @Schema(description = "Payment amount in PLN", example = "50.00")
        Double amount,

        @Schema(description = "Optional note", example = "Payment for school trip")
        String note,

        @Schema(description = "Contribution status", example = "COMPLETED")
        ContributionStatus status,

        @Schema(description = "When contribution was created")
        Instant createdAt,

        @Schema(description = "When contribution was processed")
        Instant processedAt
) {
}
