package com.schoolmoney.pl.modules.finance.attachments.services;

import com.schoolmoney.pl.files.storage.services.StorageService;
import com.schoolmoney.pl.modules.finance.attachments.management.AttachmentNotFoundException;
import com.schoolmoney.pl.modules.finance.attachments.management.CollectionAttachmentRepository;
import com.schoolmoney.pl.modules.finance.attachments.models.CollectionAttachmentDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachmentDeleteService {
    private final CollectionAttachmentRepository attachmentRepository;
    private final StorageService storageService;

    @Transactional
    public void deleteAttachment(UUID collectionId, UUID attachmentId) {
        log.info("Deleting attachment {} from collection {}", attachmentId, collectionId);

        CollectionAttachmentDAO attachment = attachmentRepository.findById(attachmentId)
                .filter(a -> a.getCollection().getId().equals(collectionId))
                .orElseThrow(AttachmentNotFoundException::new);

        storageService.deleteFile(attachment.getFilePath());
        attachmentRepository.delete(attachment);

        log.info("Attachment {} deleted successfully", attachmentId);
    }
}
