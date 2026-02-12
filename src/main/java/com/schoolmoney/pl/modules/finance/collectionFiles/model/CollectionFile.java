package com.schoolmoney.pl.modules.finance.collectionFiles.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "collection_files")
@Getter @Setter
public class CollectionFile {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID collectionId;

    @Column(nullable = false)
    private String s3Key;

    private String originalFilename;
    private String contentType;
    private Long size;

    private UUID uploadedBy;
    private LocalDateTime uploadedAt = LocalDateTime.now();
}