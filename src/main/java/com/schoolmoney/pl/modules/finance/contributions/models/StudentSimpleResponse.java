package com.schoolmoney.pl.modules.finance.contributions.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record StudentSimpleResponse(
        @Schema(description = "Student ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID studentId,

        @Schema(description = "Student's first name", example = "Jan")
        String firstName,

        @Schema(description = "Student's last name", example = "Kowalski")
        String lastName,

        @Schema(description = "Student's avatar", example = "https://example.com/avatar.png")
        String avatar
) {
}
