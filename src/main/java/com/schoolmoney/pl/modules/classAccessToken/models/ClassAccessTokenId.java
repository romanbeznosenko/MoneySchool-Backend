package com.schoolmoney.pl.modules.classAccessToken.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class ClassAccessTokenId {
    UUID id;
}
