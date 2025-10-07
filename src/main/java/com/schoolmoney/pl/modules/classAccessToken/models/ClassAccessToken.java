package com.schoolmoney.pl.modules.classAccessToken.models;

import com.schoolmoney.pl.modules.classMember.models.ClassMemberId;
import com.schoolmoney.pl.modules.classes.models.Class;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassAccessToken {
    private ClassAccessTokenId classAccessTokenId;
    private Class aClass;
    private String token;
    private Instant expireAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isArchived;
    private Instant archivedAt;

}
