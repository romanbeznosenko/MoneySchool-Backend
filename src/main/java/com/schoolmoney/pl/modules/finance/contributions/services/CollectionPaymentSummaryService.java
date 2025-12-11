package com.schoolmoney.pl.modules.finance.contributions.services;

import com.schoolmoney.pl.modules.classMember.management.ClassMemberManager;
import com.schoolmoney.pl.modules.classMember.management.ClassMemberSpecifications;
import com.schoolmoney.pl.modules.classMember.models.ClassMemberDAO;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionManager;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionNotFoundException;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionDAO;
import com.schoolmoney.pl.modules.finance.contributions.management.ContributionManager;
import com.schoolmoney.pl.modules.finance.contributions.models.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollectionPaymentSummaryService {
    private final CollectionManager collectionManager;
    private final ContributionManager contributionManager;
    private final ClassMemberManager classMemberManager;

    public CollectionPaymentSummaryResponse getPaymentSummary(UUID collectionId) {
        log.info("Getting payment summary for collection: {}", collectionId);

        // Load collection
        CollectionDAO collection = collectionManager.findById(collectionId)
                .orElseThrow(CollectionNotFoundException::new);

        // Get all confirmed students in the class
        Specification<ClassMemberDAO> spec = ClassMemberSpecifications
                .findByClassId(collection.getAClass().getId())
                .and(ClassMemberSpecifications.findConfirmed())
                .and(ClassMemberSpecifications.findNotArchived());

        List<ClassMemberDAO> classMembers = classMemberManager
                .findAll(spec, PageRequest.of(0, Integer.MAX_VALUE))
                .getContent();

        int totalStudents = classMembers.size();

        if (totalStudents == 0) {
            return buildEmptySummary(collection);
        }

        // Calculate per-student goal
        double perStudentGoal = collection.getGoal().doubleValue() / totalStudents;

        // Get total collected
        double totalCollected = contributionManager.sumByCollectionAndStatus(
                collection.getId(),
                ContributionStatus.COMPLETED
        );

        // Build per-student summaries
        List<StudentPaymentSummary> studentSummaries = new ArrayList<>();
        int studentsPaidInFull = 0;

        for (ClassMemberDAO member : classMembers) {
            StudentPaymentSummary summary = buildStudentPaymentSummary(
                    collection.getId(),
                    member.getStudent(),
                    perStudentGoal
            );
            studentSummaries.add(summary);

            if (summary.isPaidInFull()) {
                studentsPaidInFull++;
            }
        }

        return CollectionPaymentSummaryResponse.builder()
                .collectionId(collection.getId())
                .collectionTitle(collection.getTitle())
                .totalGoal(collection.getGoal())
                .totalCollected(totalCollected)
                .remainingAmount(Math.max(0, collection.getGoal() - totalCollected))
                .status(collection.getStatus())
                .totalStudents(totalStudents)
                .studentsPaidInFull(studentsPaidInFull)
                .students(studentSummaries)
                .build();
    }

    private StudentPaymentSummary buildStudentPaymentSummary(
            UUID collectionId,
            com.schoolmoney.pl.core.student.models.StudentDAO student,
            double perStudentGoal
    ) {
        // Get total paid for this student
        double amountPaid = contributionManager.sumByCollectionAndStudentAndStatus(
                collectionId,
                student.getId(),
                ContributionStatus.COMPLETED
        );

        double remainingAmount = Math.max(0, perStudentGoal - amountPaid);
        boolean isPaidInFull = amountPaid >= perStudentGoal;

        // Get contributions count
        int contributionsCount = contributionManager.countByCollectionAndStudentAndStatus(
                collectionId,
                student.getId(),
                ContributionStatus.COMPLETED
        );

        // Get recent contributions (last 3)
        List<ContributionDAO> contributions = contributionManager
                .findByCollectionIdAndStudentId(collectionId, student.getId());

        List<ContributionSimpleResponse> recentContributions = contributions.stream()
                .limit(3)
                .map(c -> ContributionSimpleResponse.builder()
                        .contributionId(c.getId())
                        .payer(UserSimpleResponse.builder()
                                .userId(c.getPayer().getId())
                                .name(c.getPayer().getName())
                                .surname(c.getPayer().getSurname())
                                .avatar(c.getPayer().getAvatar())
                                .build())
                        .amount(c.getAmount())
                        .createdAt(c.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return StudentPaymentSummary.builder()
                .studentId(student.getId())
                .studentFirstName(student.getFirstName())
                .studentLastName(student.getLastName())
                .studentAvatar(student.getAvatar())
                .perStudentGoal(perStudentGoal)
                .amountPaid(amountPaid)
                .remainingAmount(remainingAmount)
                .isPaidInFull(isPaidInFull)
                .contributionsCount(contributionsCount)
                .lastPaymentDate(contributions.isEmpty() ? null : contributions.get(0).getCreatedAt())
                .recentContributions(recentContributions)
                .build();
    }

    private CollectionPaymentSummaryResponse buildEmptySummary(CollectionDAO collection) {
        return CollectionPaymentSummaryResponse.builder()
                .collectionId(collection.getId())
                .collectionTitle(collection.getTitle())
                .totalGoal(collection.getGoal())
                .totalCollected(0.0)
                .remainingAmount(collection.getGoal().doubleValue())
                .status(collection.getStatus())
                .totalStudents(0)
                .studentsPaidInFull(0)
                .students(new ArrayList<>())
                .build();
    }
}
