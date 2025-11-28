package com.schoolmoney.pl.core.accountActivation.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record VerificationCodeResendRequest(
        @Email
        @Size(max = 128)
        @Schema(description = "User email", example = "user@email.com")
        String email) {
}
