package com.schoolmoney.pl.core.authAccount.services;

import com.schoolmoney.pl.core.authAccount.models.AuthAccount;
import com.schoolmoney.pl.core.authAccount.models.AuthAccountId;
import com.schoolmoney.pl.utils.enums.AuthTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthAccountBuilders {
    public static AuthAccount buildAuthAccount(String email, String password, AuthTypeEnum authType) {
        return AuthAccount.builder()
                          .authAccountId(AuthAccountId.of(null))
                          .isActivated(false)
                          .email(email)
                          .password(password)
                          .authType(authType)
                          .build();
    }
}
