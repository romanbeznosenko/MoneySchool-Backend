package com.schoolmoney.pl.core.user.services;

import com.schoolmoney.pl.core.user.models.User;
import com.schoolmoney.pl.core.user.models.UserId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserBuilders {
    public static User buildUserFromEmail(String email) {

        return User.builder()
                   .userId(UserId.of(null))
                   .email(email.strip()
                               .toLowerCase())
                   .isArchived(false)
                   .build();
    }
}
