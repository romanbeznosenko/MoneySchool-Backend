package com.schoolmoney.pl.modules.classes.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class ClassId {
    UUID id;
}
