package com.schoolmoney.pl.modules.finance.attachments.management;

public class InvalidFileTypeException extends RuntimeException {
    public InvalidFileTypeException() {
        super("Invalid file type. Allowed: PDF, Word, Excel, Images");
    }
}
