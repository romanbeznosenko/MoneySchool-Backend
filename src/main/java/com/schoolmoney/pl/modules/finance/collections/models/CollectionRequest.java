package com.schoolmoney.pl.modules.finance.collections.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.Instant;

@Builder
public record CollectionRequest(
        @NotBlank
        @Schema(description = "Collection title", example = "Field trip to Gdansk")
        String title,

        @Schema(description = "Collection description")
        String description,

        @Positive
        @Schema(description = "Collection goal", example = "1000")
        Long goal,

        @Schema(description = "Collection deadline", example = "2025-12-31T23:59:59Z")
        Instant deadline
) {
}
