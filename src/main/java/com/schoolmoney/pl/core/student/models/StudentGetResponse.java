package com.schoolmoney.pl.core.student.models;

import com.schoolmoney.pl.core.user.models.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record StudentGetResponse(
        @Schema(description = "Id assigned to student", example = "1")
        UUID id,

        @Schema(description = "Student's first name", example = "Jan")
        String firstName,

        @Schema(description = "Student's last name", example = "Kowalski")
        String lastName,

        @Schema(description = "Student's birth date", example = "2004-08-19")
        LocalDate birthDate,

        @Schema(description = "Student's avatar", example = "http://example.com/example.png")
        String avatar,

        @Schema(description = "Student's parent", implementation = UserResponse.class)
        UserResponse parent
) {
}
