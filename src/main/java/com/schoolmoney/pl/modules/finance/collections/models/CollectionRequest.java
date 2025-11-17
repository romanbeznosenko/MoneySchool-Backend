package com.schoolmoney.pl.modules.finance.collections.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CollectionRequest(
        @NotBlank
        @Schema(description = "Collection title", example = "Field trip to Gdansk")
        String title,

        @Schema(description = "Collection description")
        String description
) {
}
