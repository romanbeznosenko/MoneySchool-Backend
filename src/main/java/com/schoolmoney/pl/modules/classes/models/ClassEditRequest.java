package com.schoolmoney.pl.modules.classes.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ClassEditRequest(
        @Schema(description = "New class name", example = "3A")
        @NotEmpty
        String name,

        @Schema(description = "New treasurer id", example = "1")
        @NotEmpty
        UUID treasurerId
) {
}
