package com.schoolmoney.pl.modules.finance.attachments.services;

import com.schoolmoney.pl.modules.finance.attachments.management.CollectionAttachmentRepository;
import com.schoolmoney.pl.modules.finance.attachments.models.AttachmentResponse;
import com.schoolmoney.pl.modules.finance.attachments.models.CollectionAttachmentDAO;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionManager;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachmentGetService {
    private final CollectionManager collectionManager;
    private final CollectionAttachmentRepository attachmentRepository;

    public List<AttachmentResponse> getAttachments(UUID collectionId) {
        log.info("Getting attachments for collection: {}", collectionId);

        collectionManager.findById(collectionId)
                .orElseThrow(CollectionNotFoundException::new);

        List<CollectionAttachmentDAO> attachments = attachmentRepository.findByCollectionId(collectionId);

        return attachments.stream()
                .map(attachment -> new AttachmentResponse(
                        attachment.getId(),
                        attachment.getFilename(),
                        attachment.getContentType(),
                        attachment.getFileSize(),
                        attachment.getUploadedAt()
                ))
                .collect(Collectors.toList());
    }
}
