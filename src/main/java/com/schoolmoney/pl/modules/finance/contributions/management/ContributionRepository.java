package com.schoolmoney.pl.modules.finance.contributions.management;

import com.schoolmoney.pl.modules.finance.contributions.models.ContributionDAO;
import com.schoolmoney.pl.modules.finance.contributions.models.ContributionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ContributionRepository extends JpaRepository<ContributionDAO, UUID> {

    // Sum contributions by collection
    @Query("SELECT COALESCE(SUM(c.amount), 0.0) FROM ContributionDAO c " +
           "WHERE c.collection.id = :collectionId AND c.status = :status")
    Double sumByCollectionAndStatus(
            @Param("collectionId") UUID collectionId,
            @Param("status") ContributionStatus status
    );

    // Sum contributions by collection and student
    @Query("SELECT COALESCE(SUM(c.amount), 0.0) FROM ContributionDAO c " +
           "WHERE c.collection.id = :collectionId " +
           "AND c.student.id = :studentId " +
           "AND c.status = :status")
    Double sumByCollectionAndStudentAndStatus(
            @Param("collectionId") UUID collectionId,
            @Param("studentId") UUID studentId,
            @Param("status") ContributionStatus status
    );

    // Find all contributions for a collection
    Page<ContributionDAO> findByCollectionIdOrderByCreatedAtDesc(
            UUID collectionId,
            Pageable pageable
    );

    // Find all contributions for a collection (non-paginated)
    List<ContributionDAO> findByCollectionIdOrderByCreatedAtDesc(UUID collectionId);

    // Find contributions by collection and student
    List<ContributionDAO> findByCollectionIdAndStudentIdOrderByCreatedAtDesc(
            UUID collectionId,
            UUID studentId
    );

    // Find contributions by student
    List<ContributionDAO> findByStudentIdOrderByCreatedAtDesc(UUID studentId);

    // Find contributions by payer
    Page<ContributionDAO> findByPayerIdOrderByCreatedAtDesc(
            UUID payerId,
            Pageable pageable
    );

    // Find contributions by payer and collection
    Page<ContributionDAO> findByPayerIdAndCollectionIdOrderByCreatedAtDesc(
            UUID payerId,
            UUID collectionId,
            Pageable pageable
    );

    // Count distinct payers for a collection
    @Query("SELECT COUNT(DISTINCT c.payer.id) FROM ContributionDAO c " +
           "WHERE c.collection.id = :collectionId AND c.status = :status")
    Integer countDistinctPayersByCollectionAndStatus(
            @Param("collectionId") UUID collectionId,
            @Param("status") ContributionStatus status
    );

    // Count contributions by collection and student
    @Query("SELECT COUNT(c) FROM ContributionDAO c " +
           "WHERE c.collection.id = :collectionId " +
           "AND c.student.id = :studentId " +
           "AND c.status = :status")
    Integer countByCollectionAndStudentAndStatus(
            @Param("collectionId") UUID collectionId,
            @Param("studentId") UUID studentId,
            @Param("status") ContributionStatus status
    );
}
