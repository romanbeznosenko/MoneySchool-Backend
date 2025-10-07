package com.schoolmoney.pl.modules.classMember.management;

public class ClassMemberIncorrectAccessCodeException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Access code is incorrect.";
    public ClassMemberIncorrectAccessCodeException() {
        super(DEFAULT_MESSAGE);
    }
}
