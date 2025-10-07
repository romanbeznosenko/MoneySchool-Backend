package com.schoolmoney.pl.modules.classes.models;

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
public class Class {
    private ClassId classId;
    private User treasurer;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isArchived;
    private Instant archivedAt;
}
