package com.schoolmoney.pl.core.student.service;

import com.schoolmoney.pl.core.student.management.StudentManager;
import com.schoolmoney.pl.core.student.management.StudentSpecifications;
import com.schoolmoney.pl.core.student.models.StudentDAO;
import com.schoolmoney.pl.core.student.models.StudentGetPageResponse;
import com.schoolmoney.pl.core.student.models.StudentGetResponse;
import com.schoolmoney.pl.core.user.management.UserMapper;
import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.core.user.models.UserResponse;
import com.schoolmoney.pl.files.storage.services.StorageService;
import com.schoolmoney.pl.modules.classes.management.ClassManager;
import com.schoolmoney.pl.modules.classes.management.ClassSpecifications;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentGetService {
    private final StudentManager studentManager;
    private final UserMapper userMapper;
    private final HttpServletRequest request;
    private final StorageService storageService;
    private final ClassManager classManager;

    public StudentGetPageResponse getUserStudents(int page, int limit){
        UserDAO userDAO = (UserDAO)request.getAttribute("user");

        Specification<StudentDAO> specification = StudentSpecifications.findByParentId(userDAO.getId());
        Pageable pageable = PageRequest.of(page - 1, limit);

        Page<StudentDAO> studentDAOPage = studentManager.findAll(specification, pageable);

        List<StudentGetResponse> response = studentDAOPage.get()
                .map(student -> {
                    UserResponse userResponse = userMapper.mapToResponseFromEntity(
                            student.getParent(),
                            new CycleAvoidingMappingContext(),
                            storageService
                    );

                    Page<ClassDAO> classDAOPage = classManager.findAll(
                            ClassSpecifications.hasStudentMember(student.getId()),
                            PageRequest.of(0, Integer.MAX_VALUE)
                    );

                    List<ClassGetResponse> classGetResponses = classDAOPage.get()
                            .map(classDAO -> ClassGetResponse.builder()
                                    .id(classDAO.getId())
                                    .name(classDAO.getName())
                                    .treasurer(
                                            userMapper.mapToResponseFromEntity(
                                                    classDAO.getTreasurer(),
                                                    new CycleAvoidingMappingContext(),
                                                    storageService)
                                    )
                                    .build())
                            .toList();

                    return StudentGetResponse.builder()
                            .id(student.getId())
                            .firstName(student.getFirstName())
                            .lastName(student.getLastName())
                            .avatar(student.getAvatar())
                            .birthDate(student.getBirthDate())
                            .parent(userResponse)
                            .classes(classGetResponses)
                            .build();
                })
                .toList();

        return StudentGetPageResponse.builder()
                .count(studentDAOPage.getTotalElements())
                .data(response)
                .build();
    }
}
