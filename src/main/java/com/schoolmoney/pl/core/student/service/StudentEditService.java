package com.schoolmoney.pl.core.student.service;

import com.schoolmoney.pl.core.student.management.StudentManager;
import com.schoolmoney.pl.core.student.management.StudentNotFoundException;
import com.schoolmoney.pl.core.student.management.StudentParentMismatchException;
import com.schoolmoney.pl.core.student.models.StudentDAO;
import com.schoolmoney.pl.core.student.models.StudentEditRequest;
import com.schoolmoney.pl.core.user.management.UserManager;
import com.schoolmoney.pl.core.user.management.UserNotFoundException;
import com.schoolmoney.pl.core.user.models.UserDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentEditService {
    private final StudentManager studentManager;
    private final HttpServletRequest request;
    private final UserManager userManager;

    public void editStudent(UUID studentId, StudentEditRequest studentEditRequest) throws UserNotFoundException {
        UserDAO userDAO = (UserDAO)request.getAttribute("user");
        StudentDAO studentDAO = studentManager.findById(studentId)
                .orElseThrow(StudentNotFoundException::new);

        if (!studentDAO.getParent().equals(userDAO)){
            throw new StudentParentMismatchException();
        }

        studentDAO.setFirstName(studentEditRequest.firstName());
        studentDAO.setLastName(studentEditRequest.lastName());
        studentDAO.setBirthDate(studentEditRequest.birthDate());

        studentManager.saveToDatabase(studentDAO);
    }
}
