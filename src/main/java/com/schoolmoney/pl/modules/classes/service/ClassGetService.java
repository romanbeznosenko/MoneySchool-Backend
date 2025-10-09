package com.schoolmoney.pl.modules.classes.service;

import com.schoolmoney.pl.core.student.management.StudentManager;
import com.schoolmoney.pl.core.student.models.StudentDAO;
import com.schoolmoney.pl.core.user.management.UserMapper;
import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.core.user.models.UserResponse;
import com.schoolmoney.pl.files.storage.services.StorageService;
import com.schoolmoney.pl.modules.classes.management.ClassManager;
import com.schoolmoney.pl.modules.classes.management.ClassSpecifications;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import com.schoolmoney.pl.modules.classes.models.ClassGetPageResponse;
import com.schoolmoney.pl.modules.classes.models.ClassGetResponse;
import com.schoolmoney.pl.modules.classMember.management.ClassMemberManager;
import com.schoolmoney.pl.modules.classMember.models.ClassMemberDAO;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClassGetService {
    private final ClassManager classManager;
    private final StudentManager studentManager;
    private final ClassMemberManager classMemberManager;
    private final UserMapper userMapper;
    private final HttpServletRequest request;
    private final StorageService storageService;

    public ClassGetPageResponse getUserClasses(int page, int limit, boolean isTreasurer) {
        log.info("Getting user classes (isTreasurer: {})", isTreasurer);
        UserDAO userDAO = (UserDAO) request.getAttribute("user");

        Pageable pageable = PageRequest.of(page - 1, limit);
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

        List<ClassDAO> classesList = new ArrayList<>(allClasses);

        classesList.sort(Comparator.comparing(ClassDAO::getName));

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), classesList.size());
        List<ClassDAO> paginatedList = classesList.subList(start, end);

        List<ClassGetResponse> responseList = paginatedList.stream()
                .map(item -> {
                    UserResponse userResponse = userMapper.mapToResponseFromEntity(
                            item.getTreasurer(),
                            new CycleAvoidingMappingContext(),
                            storageService
                    );

                    boolean isUserTreasurer = item.getTreasurer() != null &&
                            item.getTreasurer().getId().equals(userDAO.getId());

                    return ClassGetResponse.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .treasurer(userResponse)
                            .isTreasurer(isUserTreasurer)
                            .build();
                })
                .toList();

        return ClassGetPageResponse.builder()
                .count(classesList.size())
                .data(responseList)
                .build();
    }
}