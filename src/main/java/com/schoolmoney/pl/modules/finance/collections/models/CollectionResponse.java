package com.schoolmoney.pl.modules.finance.collections.models;

import com.schoolmoney.pl.modules.classes.models.ClassGetResponse;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountGetResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record CollectionResponse(
        @Schema(description = "Collection ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID collectionId,

        @Schema(description = "Collection title", example = "Field trip to Gdansk")
        String title,

        @Schema(description = "Collection description")
        String description,

        @Schema(description = "Collection goal amount", example = "1000")
        Long goal,

        @Schema(description = "Collection logo URL")
        String logo,

        @Schema(description = "Collection status", example = "ACTIVE")
        CollectionStatus status,

        @Schema(description = "Class information", implementation = ClassGetResponse.class)
        ClassGetResponse aClass,

        @Schema(description = "Finance account for this collection", implementation = FinanceAccountGetResponse.class)
        FinanceAccountGetResponse financeAccount,

        @Schema(description = "Total amount collected", example = "750.00")
        Double totalCollected,

        @Schema(description = "Remaining amount to reach goal", example = "250.00")
        Double remainingAmount,

        @Schema(description = "Per-student goal amount", example = "20.00")
        Double perStudentGoal,

        @Schema(description = "Total number of students in class", example = "50")
        Integer totalStudentsCount,

        @Schema(description = "Number of students paid in full", example = "37")
        Integer studentsPaidInFullCount,

        @Schema(description = "When goal was reached")
        Instant goalReachedAt
) {
}
