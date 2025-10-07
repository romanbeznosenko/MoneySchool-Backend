package com.schoolmoney.pl.modules.classes.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ClassAccessCodeResponse(
        @Schema(description = "4 digit access code to class", example = "1234")
        String code
) {
}
