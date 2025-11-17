package com.schoolmoney.pl.modules.finance.collections.management;

public class CollectionNotFoundException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Collection not found";
    public CollectionNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
