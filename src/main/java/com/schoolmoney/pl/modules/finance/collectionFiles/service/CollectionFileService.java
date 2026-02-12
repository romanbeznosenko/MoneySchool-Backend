package com.schoolmoney.pl.modules.finance.collectionFiles.service;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.files.storage.services.StorageService;
import com.schoolmoney.pl.modules.classMember.management.ClassMemberRepository;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import com.schoolmoney.pl.modules.finance.collectionFiles.model.CollectionFile;
import com.schoolmoney.pl.modules.finance.collectionFiles.repository.CollectionFileRepository;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionRepository;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionDAO;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CollectionFileService {

    private final StorageService storageService;
    private final CollectionFileRepository collectionFileRepository;
    private final CollectionRepository collectionRepository;
    private final ClassMemberRepository classMemberRepository;

    @Transactional
    public void uploadPdf(
            UUID collectionId,
            UserDAO user,
            MultipartFile file
    ) throws IOException {

        CollectionDAO collection = collectionRepository
                .findById(collectionId)
                .orElseThrow();


        ClassDAO aClass = collection.getAClass();

        if (!aClass.getTreasurer().getId().equals(user.getId())) {
            ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only treasurer can upload files");
            return;
        }

        if (!"application/pdf".equals(file.getContentType())) {
            ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only PDF allowed");
            return;
        }

        UUID fileId = UUID.randomUUID();
        String key = "collections/" + collectionId + "/" + fileId + ".pdf";

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(file.getBytes());

        storageService.uploadFile(
                key,
                aClass.getId(),
                file.getContentType(),
                stream
        );

        CollectionFile entity = new CollectionFile();
        entity.setId(fileId);
        entity.setCollectionId(collectionId);
        entity.setS3Key(key);
        entity.setOriginalFilename(file.getOriginalFilename());
        entity.setSize(file.getSize());
        entity.setUploadedBy(user.getId());
        collectionFileRepository.save(entity);

    }

    public List<CollectionFile> listFiles(
            UUID collectionId,
            UserDAO user
    ) throws AccessDeniedException {
        CollectionDAO collection = collectionRepository
                .findById(collectionId)
                .orElseThrow();

        return collectionFileRepository.findByCollectionId(collectionId);
    }


    public String getPresignedUrl(
            UUID collectionId,
            UUID fileId,
            UserDAO user
    ) {
        CollectionFile file = collectionFileRepository
                .findById(fileId)
                .orElseThrow();

        if (!file.getCollectionId().equals(collectionId)) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Collection not found");
            return "";
        }

        return storageService.createPresignedGetUrl(file.getS3Key());
    }

}