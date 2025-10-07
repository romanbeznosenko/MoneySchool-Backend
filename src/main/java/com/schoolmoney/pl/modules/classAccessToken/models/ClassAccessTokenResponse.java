package com.schoolmoney.pl.modules.classAccessToken.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ClassAccessTokenResponse(
        @Schema(description = "Class access token", example = "1234")
        String token
) {
}
