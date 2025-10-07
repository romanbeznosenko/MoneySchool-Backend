package com.schoolmoney.pl.core.student.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record StudentEditRequest(
        @Schema(description = "New student first name", example = "Jan")
        @NotEmpty
        String firstName,

        @Schema(description = "New student last name", example = "Kowalski")
        @NotEmpty
        String lastName,

        @Schema(description = "New student birth date", example = "2004-08-19")
        LocalDate birthDate,

        @Schema(description = "New student parent id", example = "1")
        UUID parentId
) {
}
