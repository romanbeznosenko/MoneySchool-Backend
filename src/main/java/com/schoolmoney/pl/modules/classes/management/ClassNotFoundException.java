package com.schoolmoney.pl.modules.classes.management;

public class ClassNotFoundException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Class not found.";
    public ClassNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
