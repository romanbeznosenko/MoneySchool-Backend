package com.schoolmoney.pl.core.student.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record StudentCreateRequest(
        @Schema(description = "Student's first name", example = "Jan")
        String firstName,

        @Schema(description = "Student's last name", example = "Kowalski")
        String lastName,

        @Schema(description = "Student's birth date", example = "2004-08-19")
        LocalDate birthDate
) {
}
