package com.schoolmoney.pl.modules.classMember.management;

public class ClassMemberNotFoundException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Class Member Not Found";
    public ClassMemberNotFoundException(){
        super(DEFAULT_MESSAGE);
    }
}
