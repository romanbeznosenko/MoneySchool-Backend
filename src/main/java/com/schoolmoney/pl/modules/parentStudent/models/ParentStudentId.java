package com.schoolmoney.pl.modules.parentStudent.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class ParentStudentId {
    UUID id;
}
