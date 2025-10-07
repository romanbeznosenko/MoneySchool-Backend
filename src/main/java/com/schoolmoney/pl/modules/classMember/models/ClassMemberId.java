package com.schoolmoney.pl.modules.classMember.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class ClassMemberId {
    UUID id;
}
