package com.schoolmoney.pl.core.login.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest(
        @NotBlank
        @Email
        @Schema(description = "User email", example = "user@email.com")
        String email,

        @NotBlank
        @Schema(description = "User password", example = "Mariusz!Pudzianowski69")
        String password,
        @Schema(description = "Flag to indicate if user should be logged for long time", example = "false", defaultValue = "false")
        Boolean staySignedIn
) {
}
