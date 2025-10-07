package com.schoolmoney.pl.core.student.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class StudentId {
    UUID id;
}
