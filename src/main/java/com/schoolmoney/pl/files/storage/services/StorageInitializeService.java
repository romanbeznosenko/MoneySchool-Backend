package com.schoolmoney.pl.files.storage.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.BucketAlreadyExistsException;
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageInitializeService {

    private final StorageService storageService;

    @PostConstruct
    public void init() {

        log.info("Storage service initialized");
        try {
            log.info("Creating default bucket");
            storageService.createBucket();
        } catch (BucketAlreadyExistsException | BucketAlreadyOwnedByYouException e) {
            log.error("Bucket already exist");
        }

    }
}
