package com.schoolmoney.pl.modules.finance.collectionFiles.repository;

import com.schoolmoney.pl.modules.finance.collectionFiles.model.CollectionFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CollectionFileRepository
        extends JpaRepository<CollectionFile, UUID> {

    List<CollectionFile> findByCollectionId(UUID collectionId);
}