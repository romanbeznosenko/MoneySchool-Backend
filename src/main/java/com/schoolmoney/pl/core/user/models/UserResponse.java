package com.schoolmoney.pl.core.user.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponse(
        @Schema(description = "Assigned Id", example = "1")
        UUID id,

        @Schema(description = "User email", example = "example@email.com")
        String email,

        @Schema(description = "User name", example = "Kamil")
        String name,

        @Schema(description = "User surname", example = "Zdun")
        String surname,

        @Schema(description = "User avatar", example = "https://example.com/avatar.png")
        String avatar ){
}