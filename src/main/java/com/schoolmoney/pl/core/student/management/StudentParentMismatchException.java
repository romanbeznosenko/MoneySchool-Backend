package com.schoolmoney.pl.core.student.management;

public class StudentParentMismatchException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Logged user is not the parent of this student.";
    public StudentParentMismatchException() {
        super(DEFAULT_MESSAGE);
    }
}
