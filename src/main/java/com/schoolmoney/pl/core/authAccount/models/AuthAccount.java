package com.schoolmoney.pl.core.authAccount.models;

import com.schoolmoney.pl.utils.enums.AuthTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthAccount {
    private AuthAccountId authAccountId;
    private String email;
    private AuthTypeEnum authType;
    private UUID userId;
    private String password;
    private Boolean isActivated;
    private Instant createdAt;
    private Instant updatedAt;
}





