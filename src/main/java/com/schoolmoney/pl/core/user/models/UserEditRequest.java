package com.schoolmoney.pl.core.user.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record UserEditRequest(
        @Schema(description = "New user name", example = "Jan")
        @NotEmpty
        String name,

        @Schema(description = "New user surname", example = "Kowalski")
        @NotEmpty
        String surname
) {
}
