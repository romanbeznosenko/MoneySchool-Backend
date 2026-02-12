package com.schoolmoney.pl.modules.finance.collections.services;

import com.schoolmoney.pl.core.student.management.StudentManager;
import com.schoolmoney.pl.core.student.models.StudentDAO;
import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.core.user.models.UserResponse;
import com.schoolmoney.pl.modules.classMember.management.ClassMemberManager;
import com.schoolmoney.pl.modules.classMember.management.ClassMemberSpecifications;
import com.schoolmoney.pl.modules.classMember.models.ClassMemberDAO;
import com.schoolmoney.pl.modules.classes.management.ClassManager;
import com.schoolmoney.pl.modules.classes.management.ClassSpecifications;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import com.schoolmoney.pl.modules.classes.models.ClassGetResponse;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionManager;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionSpecifications;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionDAO;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionPageResponse;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionResponse;
import com.schoolmoney.pl.modules.finance.contributions.management.ContributionManager;
import com.schoolmoney.pl.modules.finance.contributions.models.ContributionStatus;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountGetResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollectionGetService {
    private final HttpServletRequest request;
    private final CollectionManager collectionManager;
    private final ClassManager classManager;
    private final StudentManager studentManager;
    private final ClassMemberManager classMemberManager;
    private final ContributionManager contributionManager;

    public CollectionPageResponse get(int limit, int page, boolean isTreasurer){
        log.info("Getting all collections");

        UserDAO userDAO = (UserDAO)request.getAttribute("user");

        PageRequest pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Set<ClassDAO> allClasses = new HashSet<>();

        if (isTreasurer) {
            Specification<ClassDAO> treasurerSpec = ClassSpecifications.findByUserTreasurer(userDAO.getId());
            Page<ClassDAO> treasurerClasses = classManager.findUserTreasurerClasses(treasurerSpec, pageable);
            allClasses.addAll(treasurerClasses.getContent());
        } else {
            Specification<ClassDAO> treasurerSpec = ClassSpecifications.findByUserTreasurer(userDAO.getId());
            Page<ClassDAO> treasurerClasses = classManager.findAll(treasurerSpec, PageRequest.of(0, Integer.MAX_VALUE));
            allClasses.addAll(treasurerClasses.getContent());

            List<StudentDAO> userStudents = studentManager.findByParentId(userDAO.getId());

            for (StudentDAO student : userStudents) {
                List<ClassMemberDAO> memberships = classMemberManager.findByStudentAndIsConfirmedAndIsArchived(
                        student,
                        true,
                        false
                );

                for (ClassMemberDAO membership : memberships) {
                    allClasses.add(membership.getAClass());
                }
            }

            log.info("Found {} total classes for user", allClasses.size());
        }

        List<ClassDAO> classesList = allClasses.stream().toList();
        pageable = PageRequest.of(page - 1, limit);
        Specification<CollectionDAO> spec = CollectionSpecifications.findByClass(classesList);

        Page<CollectionDAO> collectionsPage = collectionManager.findAll(spec, pageable);
        List<CollectionResponse> collectionResponseList = collectionsPage.get()
                .map(item -> buildCollectionResponse(item, userDAO))
                .toList();

        return CollectionPageResponse.builder()
                .count(collectionsPage.getTotalElements())
                .data(collectionResponseList)
                .build();
    }

    private CollectionResponse buildCollectionResponse(CollectionDAO collection, UserDAO currentUser) {
        // Get all confirmed students in the class
        Specification<ClassMemberDAO> memberSpec = ClassMemberSpecifications
                .findByClassId(collection.getAClass().getId())
                .and(ClassMemberSpecifications.findConfirmed())
                .and(ClassMemberSpecifications.findNotArchived());

        List<ClassMemberDAO> classMembers = classMemberManager
                .findAll(memberSpec, PageRequest.of(0, Integer.MAX_VALUE))
                .getContent();

        int totalStudents = classMembers.size();

        // Calculate per-student goal
        Double perStudentGoal = totalStudents > 0
                ? collection.getGoal().doubleValue() / totalStudents
                : null;

        // Get total collected
        Double totalCollected = contributionManager.sumByCollectionAndStatus(
                collection.getId(),
                ContributionStatus.COMPLETED
        );

        // Calculate remaining amount
        Double remainingAmount = collection.getGoal() - totalCollected;

        // Count students paid in full
        Integer studentsPaidInFull = 0;
        if (totalStudents > 0 && perStudentGoal != null) {
            for (ClassMemberDAO member : classMembers) {
                Double studentPaid = contributionManager.sumByCollectionAndStudentAndStatus(
                        collection.getId(),
                        member.getStudent().getId(),
                        ContributionStatus.COMPLETED
                );
                if (studentPaid >= perStudentGoal) {
                    studentsPaidInFull++;
                }
            }
        }

        // Build finance account response
        FinanceAccountGetResponse financeAccountResponse = null;
        if (collection.getFinanceAccount() != null) {
            financeAccountResponse = FinanceAccountGetResponse.builder()
                    .id(collection.getFinanceAccount().getId())
                    .IBAN(collection.getFinanceAccount().getIBAN())
                    .balance(collection.getFinanceAccount().getBalance())
                    .isTreasurerAccount(collection.getFinanceAccount().getIsTreasurerAccount())
                    .build();
        }

        return CollectionResponse.builder()
                .collectionId(collection.getId())
                .title(collection.getTitle())
                .description(collection.getDescription())
                .goal(collection.getGoal())
                .logo(collection.getLogo())
                .status(collection.getStatus())
                .aClass(
                        ClassGetResponse.builder()
                                .id(collection.getAClass().getId())
                                .name(collection.getAClass().getName())
                                .treasurer(
                                        UserResponse.builder()
                                                .id(collection.getAClass().getTreasurer().getId())
                                                .email(collection.getAClass().getTreasurer().getEmail())
                                                .name(collection.getAClass().getTreasurer().getName())
                                                .surname(collection.getAClass().getTreasurer().getSurname())
                                                .avatar(collection.getAClass().getTreasurer().getAvatar())
                                                .build()
                                )
                                .isTreasurer(collection.getAClass().getTreasurer().equals(currentUser))
                                .memberCount((long) totalStudents)
                                .build()
                )
                .financeAccount(financeAccountResponse)
                .totalCollected(totalCollected)
                .remainingAmount(remainingAmount)
                .perStudentGoal(perStudentGoal)
                .totalStudentsCount(totalStudents)
                .studentsPaidInFullCount(studentsPaidInFull)
                .goalReachedAt(collection.getGoalReachedAt())
                .deadline(collection.getDeadline())
                .build();
    }
}
