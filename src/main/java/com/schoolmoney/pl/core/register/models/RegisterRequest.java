package com.schoolmoney.pl.core.register.models;

import com.schoolmoney.pl.utils.validators.password.PasswordConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterRequest(
        @NotBlank
        @Email
        @Size(max = 128)
        @Schema(description = "User email", example = "user@email.com")
        String email,

        @NotBlank
        @PasswordConstraint
        @Size(max = 72)
        @Schema(description = "User password", example = "Mariusz!Pudzianowski69")
        String password) {
}
