package com.schoolmoney.pl.modules.classMember.models;

import com.schoolmoney.pl.core.student.models.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.schoolmoney.pl.modules.classes.models.Class;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassMember {
    private ClassMemberId classMemberId;
    private Class aClass;
    private Student student;
    private Instant joinedAt;
    private Boolean isConfirmed;
    private Instant updatedAt;
    private Instant createdAt;
    private Boolean isArchived;
    private Instant archivedAt;
}
