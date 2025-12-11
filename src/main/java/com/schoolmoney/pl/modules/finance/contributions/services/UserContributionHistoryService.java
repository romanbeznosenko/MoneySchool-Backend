package com.schoolmoney.pl.modules.finance.contributions.services;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.finance.contributions.management.ContributionManager;
import com.schoolmoney.pl.modules.finance.contributions.models.*;
import jakarta.servlet.http.HttpServletRequest;
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
public class UserContributionHistoryService {
    private final HttpServletRequest request;
    private final ContributionManager contributionManager;

    public ContributionListResponse getUserContributions(
            UUID collectionId,
            Pageable pageable
    ) {
        UserDAO user = (UserDAO) request.getAttribute("user");
        log.info("Getting contribution history for user: {}", user.getId());

        Page<ContributionDAO> contributionsPage;

        if (collectionId != null) {
            // Filter by specific collection
            contributionsPage = contributionManager.findByPayerIdAndCollectionId(
                    user.getId(),
                    collectionId,
                    pageable
            );
        } else {
            // All contributions by user
            contributionsPage = contributionManager.findByPayerId(user.getId(), pageable);
        }

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
