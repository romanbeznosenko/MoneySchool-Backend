package com.schoolmoney.pl.modules.finance.attachments.services;

import com.schoolmoney.pl.files.storage.services.StorageService;
import com.schoolmoney.pl.modules.finance.attachments.management.AttachmentNotFoundException;
import com.schoolmoney.pl.modules.finance.attachments.management.CollectionAttachmentRepository;
import com.schoolmoney.pl.modules.finance.attachments.models.CollectionAttachmentDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachmentDownloadService {
    private final CollectionAttachmentRepository attachmentRepository;
    private final StorageService storageService;

    public ResponseEntity<Resource> downloadAttachment(UUID attachmentId) {
        log.info("Downloading attachment: {}", attachmentId);

        CollectionAttachmentDAO attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(AttachmentNotFoundException::new);

        ResponseInputStream<GetObjectResponse> s3Object = storageService.getFile(attachment.getFilePath());

        InputStreamResource resource = new InputStreamResource(s3Object);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .body(resource);
    }
}
