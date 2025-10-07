package com.schoolmoney.pl.modules.parentStudent.models;

import com.schoolmoney.pl.core.student.models.Student;
import com.schoolmoney.pl.core.user.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentStudent {
    private ParentStudentId parentStudentId;
    private User parent;
    private Student student;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isArchived;
    private Instant archivedAt;
}
