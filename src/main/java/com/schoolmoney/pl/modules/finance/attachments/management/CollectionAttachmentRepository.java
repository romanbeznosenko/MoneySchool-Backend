package com.schoolmoney.pl.modules.finance.attachments.management;

import com.schoolmoney.pl.modules.finance.attachments.models.CollectionAttachmentDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CollectionAttachmentRepository extends JpaRepository<CollectionAttachmentDAO, UUID> {
    List<CollectionAttachmentDAO> findByCollectionId(UUID collectionId);
    long countByCollectionId(UUID collectionId);
}
