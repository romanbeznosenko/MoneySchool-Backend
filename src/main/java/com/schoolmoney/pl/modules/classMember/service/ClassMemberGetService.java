package com.schoolmoney.pl.modules.classMember.service;

import com.schoolmoney.pl.core.student.models.StudentGetResponse;
import com.schoolmoney.pl.core.user.management.UserMapper;
import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.files.storage.services.StorageService;
import com.schoolmoney.pl.modules.classMember.management.ClassMemberManager;
import com.schoolmoney.pl.modules.classMember.management.ClassMemberSpecifications;
import com.schoolmoney.pl.modules.classMember.models.ClassMemberDAO;
import com.schoolmoney.pl.modules.classMember.models.ClassMemberGetPageResponse;
import com.schoolmoney.pl.modules.classMember.models.ClassMemberGetResponse;
import com.schoolmoney.pl.modules.classes.management.ClassManager;
import com.schoolmoney.pl.modules.classes.management.ClassTreasurerMismatchException;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import com.schoolmoney.pl.modules.classes.models.ClassGetResponse;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClassMemberGetService {
    private final ClassMemberManager classMemberManager;
    private final ClassManager classManager;
    private final UserMapper userMapper;
    private final StorageService storageService;
    private final HttpServletRequest request;

    public ClassMemberGetPageResponse getClassMembers(int page, int limit, UUID classId) throws ClassNotFoundException {
        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        ClassDAO classDAO = classManager.findById(classId)
                .orElseThrow(ClassNotFoundException::new);

        // Compare IDs instead of entities
        boolean isTreasurer = classDAO.getTreasurer().getId().equals(userDAO.getId());

        // Check if user is a parent of any student in this class
        Specification<ClassMemberDAO> parentCheckSpec =
                ClassMemberSpecifications.findByClassId(classId)
                        .and(ClassMemberSpecifications.findByParentId(userDAO.getId()));

        boolean isParent = classMemberManager.count(parentCheckSpec) > 0;

        if (!isTreasurer && !isParent) {
            log.warn("User {} attempted to access class {} without permission", userDAO.getId(), classId);
            throw new ClassTreasurerMismatchException();
        }

        log.info("User {} authorized to access class {}. Treasurer: {}, Parent: {}",
                userDAO.getId(), classId, isTreasurer, isParent);

        Specification<ClassMemberDAO> specification = ClassMemberSpecifications.findByClassId(classId);
        Pageable pageable = PageRequest.of(page - 1, limit);

        Page<ClassMemberDAO> classMembers = classMemberManager.findAll(specification, pageable);

        List<ClassMemberGetResponse> list = classMembers.get()
                .map(classMemberDAO -> {
                    ClassGetResponse classGetResponse = ClassGetResponse.builder()
                            .id(classMemberDAO.getAClass().getId())
                            .name(classMemberDAO.getAClass().getName())
                            .treasurer(userMapper.mapToResponseFromEntity(
                                    classMemberDAO.getAClass().getTreasurer(),
                                    new CycleAvoidingMappingContext(),
                                    storageService
                            ))
                            .build();

                    StudentGetResponse studentGetResponse = StudentGetResponse.builder()
                            .id(classMemberDAO.getStudent().getId())
                            .firstName(classMemberDAO.getStudent().getFirstName())
                            .lastName(classMemberDAO.getStudent().getLastName())
                            .birthDate(classMemberDAO.getStudent().getBirthDate())
                            .avatar(null)
                            .parent(userMapper.mapToResponseFromEntity(
                                    classMemberDAO.getStudent().getParent(),
                                    new CycleAvoidingMappingContext(),
                                    storageService
                            ))
                            .build();

                    return ClassMemberGetResponse.builder()
                            .id(classMemberDAO.getId())
                            .clas(classGetResponse)
                            .student(studentGetResponse)
                            .build();
                })
                .toList();

        return ClassMemberGetPageResponse.builder()
                .count(classMembers.getTotalElements())
                .data(list)
                .build();
    }
}
