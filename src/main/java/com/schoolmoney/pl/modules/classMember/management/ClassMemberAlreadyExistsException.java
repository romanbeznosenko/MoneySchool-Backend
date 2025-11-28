package com.schoolmoney.pl.modules.classMember.management;

public class ClassMemberAlreadyExistsException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Student is already a member of this class.";
    public ClassMemberAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }
}
