package com.schoolmoney.pl.modules.classMember.models;

import com.schoolmoney.pl.core.student.models.StudentGetResponse;
import com.schoolmoney.pl.modules.classes.models.ClassGetResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ClassMemberGetResponse(
        @Schema(description = "Assigned id to class member", example = "1")
        UUID id,

        @Schema(description = "Class", implementation = ClassGetResponse.class)
        ClassGetResponse clas,

        @Schema(description = "Student", implementation = StudentGetResponse.class)
        StudentGetResponse student
) {
}
