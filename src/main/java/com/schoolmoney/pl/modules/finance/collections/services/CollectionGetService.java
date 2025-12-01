package com.schoolmoney.pl.modules.finance.collections.services;

import com.schoolmoney.pl.core.student.management.StudentManager;
import com.schoolmoney.pl.core.student.models.StudentDAO;
import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.core.user.models.UserResponse;
import com.schoolmoney.pl.modules.classMember.management.ClassMemberManager;
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
                .map(item -> CollectionResponse.builder()
                        .collectionId(item.getId())
                        .title(item.getTitle())
                        .description(item.getDescription())
                        .goal(item.getGoal())
                        .logo(item.getLogo())
                        .aClass(
                                ClassGetResponse.builder()
                                        .id(item.getAClass().getId())
                                        .name(item.getAClass().getName())
                                        .treasurer(
                                                UserResponse.builder()
                                                        .id(item.getAClass().getTreasurer().getId())
                                                        .email(item.getAClass().getTreasurer().getEmail())
                                                        .name(item.getAClass().getTreasurer().getName())
                                                        .surname(item.getAClass().getTreasurer().getSurname())
                                                        .avatar(null)
                                                        .build()
                                        )
                                        .isTreasurer(item.getAClass().getTreasurer().equals(userDAO))
                                        .memberCount(null)
                                        .build()
                        )
                        .build())
                .toList();

        return CollectionPageResponse.builder()
                .count(collectionsPage.getTotalElements())
                .data(collectionResponseList)
                .build();
    }
}
