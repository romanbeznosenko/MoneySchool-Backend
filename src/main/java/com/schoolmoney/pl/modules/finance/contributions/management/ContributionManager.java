package com.schoolmoney.pl.modules.finance.contributions.management;

import com.schoolmoney.pl.modules.finance.contributions.models.ContributionDAO;
import com.schoolmoney.pl.modules.finance.contributions.models.ContributionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContributionManager {
    private final ContributionRepository contributionRepository;

    public ContributionDAO saveToDatabase(ContributionDAO contribution) {
        return contributionRepository.save(contribution);
    }

    public Optional<ContributionDAO> findById(UUID id) {
        return contributionRepository.findById(id);
    }

    public Double sumByCollectionAndStatus(UUID collectionId, ContributionStatus status) {
        return contributionRepository.sumByCollectionAndStatus(collectionId, status);
    }

    public Double sumByCollectionAndStudentAndStatus(
            UUID collectionId,
            UUID studentId,
            ContributionStatus status
    ) {
        return contributionRepository.sumByCollectionAndStudentAndStatus(
                collectionId,
                studentId,
                status
        );
    }

    public Page<ContributionDAO> findByCollectionId(UUID collectionId, Pageable pageable) {
        return contributionRepository.findByCollectionIdOrderByCreatedAtDesc(
                collectionId,
                pageable
        );
    }

    public List<ContributionDAO> findByCollectionId(UUID collectionId) {
        return contributionRepository.findByCollectionIdOrderByCreatedAtDesc(collectionId);
    }

    public List<ContributionDAO> findByCollectionIdAndStudentId(
            UUID collectionId,
            UUID studentId
    ) {
        return contributionRepository.findByCollectionIdAndStudentIdOrderByCreatedAtDesc(
                collectionId,
                studentId
        );
    }

    public List<ContributionDAO> findByStudentId(UUID studentId) {
        return contributionRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
    }

    public Page<ContributionDAO> findByPayerId(UUID payerId, Pageable pageable) {
        return contributionRepository.findByPayerIdOrderByCreatedAtDesc(payerId, pageable);
    }

    public Page<ContributionDAO> findByPayerIdAndCollectionId(
            UUID payerId,
            UUID collectionId,
            Pageable pageable
    ) {
        return contributionRepository.findByPayerIdAndCollectionIdOrderByCreatedAtDesc(
                payerId,
                collectionId,
                pageable
        );
    }

    public Integer countDistinctPayersByCollectionAndStatus(
            UUID collectionId,
            ContributionStatus status
    ) {
        return contributionRepository.countDistinctPayersByCollectionAndStatus(
                collectionId,
                status
        );
    }

    public Integer countByCollectionAndStudentAndStatus(
            UUID collectionId,
            UUID studentId,
            ContributionStatus status
    ) {
        return contributionRepository.countByCollectionAndStudentAndStatus(
                collectionId,
                studentId,
                status
        );
    }
}
