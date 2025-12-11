package com.schoolmoney.pl.modules.finance.contributions.services;

import com.schoolmoney.pl.modules.finance.contributions.management.ContributionManager;
import com.schoolmoney.pl.modules.finance.contributions.models.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContributionGetService {
    private final ContributionManager contributionManager;

    public ContributionListResponse getContributionsByCollection(
            UUID collectionId,
            Pageable pageable
    ) {
        log.info("Getting contributions for collection: {}", collectionId);

        Page<ContributionDAO> contributionsPage = contributionManager
                .findByCollectionId(collectionId, pageable);

        List<ContributionResponse> contributions = contributionsPage.getContent()
                .stream()
                .map(this::buildContributionResponse)
                .collect(Collectors.toList());

        double totalAmount = contributions.stream()
                .mapToDouble(ContributionResponse::amount)
                .sum();

        return ContributionListResponse.builder()
                .count(contributionsPage.getTotalElements())
                .totalAmount(totalAmount)
                .data(contributions)
                .build();
    }

    public List<ContributionResponse> getContributionsByCollectionAndStudent(
            UUID collectionId,
            UUID studentId
    ) {
        log.info("Getting contributions for collection: {} and student: {}",
                collectionId, studentId);

        List<ContributionDAO> contributions = contributionManager
                .findByCollectionIdAndStudentId(collectionId, studentId);

        return contributions.stream()
                .map(this::buildContributionResponse)
                .collect(Collectors.toList());
    }

    private ContributionResponse buildContributionResponse(ContributionDAO contribution) {
        return ContributionResponse.builder()
                .contributionId(contribution.getId())
                .collection(CollectionSimpleResponse.builder()
                        .collectionId(contribution.getCollection().getId())
                        .title(contribution.getCollection().getTitle())
                        .logo(contribution.getCollection().getLogo())
                        .build())
                .student(StudentSimpleResponse.builder()
                        .studentId(contribution.getStudent().getId())
                        .firstName(contribution.getStudent().getFirstName())
                        .lastName(contribution.getStudent().getLastName())
                        .avatar(contribution.getStudent().getAvatar())
                        .build())
                .payer(UserSimpleResponse.builder()
                        .userId(contribution.getPayer().getId())
                        .name(contribution.getPayer().getName())
                        .surname(contribution.getPayer().getSurname())
                        .avatar(contribution.getPayer().getAvatar())
                        .build())
                .amount(contribution.getAmount())
                .note(contribution.getNote())
                .status(contribution.getStatus())
                .createdAt(contribution.getCreatedAt())
                .processedAt(contribution.getProcessedAt())
                .build();
    }
}
