package com.schoolmoney.pl.modules.finance.contributions.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserSimpleResponse(
        @Schema(description = "User ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID userId,

        @Schema(description = "User name", example = "Kamil")
        String name,

        @Schema(description = "User surname", example = "Zdun")
        String surname,

        @Schema(description = "User avatar", example = "https://example.com/avatar.png")
        String avatar
) {
}
