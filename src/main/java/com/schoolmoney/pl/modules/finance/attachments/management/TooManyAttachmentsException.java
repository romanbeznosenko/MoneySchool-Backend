package com.schoolmoney.pl.modules.finance.attachments.management;

public class TooManyAttachmentsException extends RuntimeException {
    public TooManyAttachmentsException() {
        super("Maximum 5 attachments allowed per collection");
    }
}
