package com.schoolmoney.pl.core.user.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class UserId {

    UUID id;
}
