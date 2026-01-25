package com.schoolmoney.pl.modules.finance.attachments.models;

import java.time.Instant;
import java.util.UUID;

public record AttachmentResponse(
        UUID id,
        String filename,
        String contentType,
        Long fileSize,
        Instant uploadedAt
) {
}
