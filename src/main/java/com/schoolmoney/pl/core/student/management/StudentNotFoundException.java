package com.schoolmoney.pl.core.student.management;

public class StudentNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Student not found";
    public StudentNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
