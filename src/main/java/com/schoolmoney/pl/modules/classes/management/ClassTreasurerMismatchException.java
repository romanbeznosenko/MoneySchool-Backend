package com.schoolmoney.pl.modules.classes.management;

public class ClassTreasurerMismatchException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Logged user do not have permission for this action.";
    public ClassTreasurerMismatchException() {
        super(DEFAULT_MESSAGE);
    }
}
