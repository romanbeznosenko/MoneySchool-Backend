package com.schoolmoney.pl.modules.finance.contributions.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record StudentPaymentSummary(
        @Schema(description = "Student ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID studentId,

        @Schema(description = "Student's first name", example = "Jan")
        String studentFirstName,

        @Schema(description = "Student's last name", example = "Kowalski")
        String studentLastName,

        @Schema(description = "Student's avatar", example = "https://example.com/avatar.png")
        String studentAvatar,

        @Schema(description = "Per-student goal amount", example = "20.00")
        Double perStudentGoal,

        @Schema(description = "Total amount paid for this student", example = "15.00")
        Double amountPaid,

        @Schema(description = "Remaining amount to reach goal", example = "5.00")
        Double remainingAmount,

        @Schema(description = "Whether student's goal is fully paid", example = "false")
        Boolean isPaidInFull,

        @Schema(description = "Number of contributions for this student", example = "3")
        Integer contributionsCount,

        @Schema(description = "Date of last payment for this student")
        Instant lastPaymentDate,

        @Schema(description = "Recent contributions for this student")
        List<ContributionSimpleResponse> recentContributions
) {
}
