package com.schoolmoney.pl.modules.finance.attachments.services;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.files.storage.services.StorageService;
import com.schoolmoney.pl.modules.finance.attachments.management.CollectionAttachmentRepository;
import com.schoolmoney.pl.modules.finance.attachments.management.InvalidFileTypeException;
import com.schoolmoney.pl.modules.finance.attachments.management.TooManyAttachmentsException;
import com.schoolmoney.pl.modules.finance.attachments.models.CollectionAttachmentDAO;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionManager;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionNotFoundException;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachmentUploadService {
    private final HttpServletRequest request;
    private final CollectionManager collectionManager;
    private final CollectionAttachmentRepository attachmentRepository;
    private final StorageService storageService;

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/gif",
            "image/webp"
    );

    private static final int MAX_ATTACHMENTS = 5;

    @Transactional
    public void uploadAttachments(UUID collectionId, List<MultipartFile> files) throws IOException {
        log.info("Uploading attachments for collection: {}", collectionId);
        UserDAO user = (UserDAO) request.getAttribute("user");

        // Get collection
        CollectionDAO collection = collectionManager.findById(collectionId)
                .orElseThrow(CollectionNotFoundException::new);

        // Check attachment count
        long currentCount = attachmentRepository.countByCollectionId(collectionId);
        if (currentCount + files.size() > MAX_ATTACHMENTS) {
            throw new TooManyAttachmentsException();
        }

        // Upload each file
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
                throw new InvalidFileTypeException();
            }

            // Generate unique key for storage
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String storageKey = "collections/" + collectionId + "/attachments/" + UUID.randomUUID() + fileExtension;

            // Upload to storage
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(file.getBytes());
            storageService.uploadFile(storageKey, collection.getAClass().getSchool().getId(), contentType, outputStream);

            // Save attachment metadata
            CollectionAttachmentDAO attachment = CollectionAttachmentDAO.builder()
                    .collection(collection)
                    .filename(originalFilename)
                    .contentType(contentType)
                    .filePath(storageKey)
                    .fileSize(file.getSize())
                    .build();

            attachmentRepository.save(attachment);
            log.info("Attachment uploaded: {} ({})", originalFilename, storageKey);
        }

        log.info("All attachments uploaded successfully for collection: {}", collectionId);
    }
}
