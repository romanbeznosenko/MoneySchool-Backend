package com.schoolmoney.pl.core.student.service;

import com.schoolmoney.pl.core.student.management.StudentManager;
import com.schoolmoney.pl.core.student.models.StudentCreateRequest;
import com.schoolmoney.pl.core.student.models.StudentDAO;
import com.schoolmoney.pl.core.user.models.UserDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentCreateService {
    private final StudentManager studentManager;
    private final HttpServletRequest request;

    public void createStudent(StudentCreateRequest studentCreateRequest){
        UserDAO userDAO = (UserDAO) request.getAttribute("user");

        StudentDAO studentDAO = StudentDAO.builder()
                .firstName(studentCreateRequest.firstName())
                .lastName(studentCreateRequest.lastName())
                .birthDate(studentCreateRequest.birthDate())
                .parent(userDAO)
                .isArchived(false)
                .build();

        studentManager.saveToDatabase(studentDAO);
    }
}
