package com.schoolmoney.pl.core.login.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record LoginResponse(
        @Schema(description = "Access token")
        String jwt,

        @Schema(description = "Refresh token")
        UUID refreshToken) {
}
