package com.schoolmoney.pl.modules.finance.attachments.management;

public class AttachmentNotFoundException extends RuntimeException {
    public AttachmentNotFoundException() {
        super("Attachment not found");
    }
}
