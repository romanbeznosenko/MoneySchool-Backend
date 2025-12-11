package com.schoolmoney.pl.modules.finance.contributions.models;

import com.schoolmoney.pl.modules.finance.collections.models.CollectionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record CollectionPaymentSummaryResponse(
        @Schema(description = "Collection ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID collectionId,

        @Schema(description = "Collection title", example = "Field trip to Gdansk")
        String collectionTitle,

        @Schema(description = "Total goal amount", example = "100")
        Long totalGoal,

        @Schema(description = "Total amount collected", example = "75.00")
        Double totalCollected,

        @Schema(description = "Remaining amount to goal", example = "25.00")
        Double remainingAmount,

        @Schema(description = "Collection status", example = "ACTIVE")
        CollectionStatus status,

        @Schema(description = "Total number of students in class", example = "5")
        Integer totalStudents,

        @Schema(description = "Number of students paid in full", example = "3")
        Integer studentsPaidInFull,

        @Schema(description = "Payment summary per student")
        List<StudentPaymentSummary> students
) {
}
