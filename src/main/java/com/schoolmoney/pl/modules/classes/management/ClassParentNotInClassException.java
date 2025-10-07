package com.schoolmoney.pl.modules.classes.management;

public class ClassParentNotInClassException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "User not found in parents of class.";
    public ClassParentNotInClassException() {
        super(DEFAULT_MESSAGE);
    }
}
