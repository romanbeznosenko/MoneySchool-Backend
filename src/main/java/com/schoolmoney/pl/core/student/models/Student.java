package com.schoolmoney.pl.core.student.models;

import com.schoolmoney.pl.core.user.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private StudentId studentId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private User parent;
    private String avatar;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant archivedAt;
    private Boolean isArchived;
}
