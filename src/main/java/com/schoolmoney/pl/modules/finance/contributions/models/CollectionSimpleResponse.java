package com.schoolmoney.pl.modules.finance.contributions.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CollectionSimpleResponse(
        @Schema(description = "Collection ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID collectionId,

        @Schema(description = "Collection title", example = "Field trip to Gdansk")
        String title,

        @Schema(description = "Collection logo URL", example = "https://example.com/logo.png")
        String logo
) {
}
