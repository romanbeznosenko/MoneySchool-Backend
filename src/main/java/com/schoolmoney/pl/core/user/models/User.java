package com.schoolmoney.pl.core.user.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UserId userId;
    private String email;
    private String name;
    private String surname;
    private String avatar;

    private Boolean isArchived;
    private Instant archivedAt;

    private Instant createdAt;
    private Instant updatedAt;
}





