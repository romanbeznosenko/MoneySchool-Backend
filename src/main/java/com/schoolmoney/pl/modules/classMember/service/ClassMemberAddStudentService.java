package com.schoolmoney.pl.modules.classMember.service;

import com.schoolmoney.pl.core.student.management.StudentManager;
import com.schoolmoney.pl.core.student.management.StudentNotFoundException;
import com.schoolmoney.pl.core.student.management.StudentParentMismatchException;
import com.schoolmoney.pl.core.student.models.StudentDAO;
import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.classAccessToken.management.ClassAccessTokenManager;
import com.schoolmoney.pl.modules.classAccessToken.management.ClassAccessTokenSpecifications;
import com.schoolmoney.pl.modules.classAccessToken.models.ClassAccessTokenDAO;
import com.schoolmoney.pl.modules.classMember.management.ClassMemberAlreadyExistsException;
import com.schoolmoney.pl.modules.classMember.management.ClassMemberIncorrectAccessCodeException;
import com.schoolmoney.pl.modules.classMember.management.ClassMemberManager;
import com.schoolmoney.pl.modules.classMember.models.ClassMemberDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClassMemberAddStudentService {
    private final HttpServletRequest request;
    private final StudentManager studentManager;
    private final ClassMemberManager classMemberManager;
    private final ClassAccessTokenManager classAccessTokenManager;

    public void addStudentToClass(UUID studentId, String accessCode) {
        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        log.info("Adding student {} with access code: {}", studentId, accessCode);

        StudentDAO studentDAO = studentManager.findById(studentId)
                .orElseThrow(StudentNotFoundException::new);

        log.info("Student parent ID: {}, User ID: {}", studentDAO.getParent().getId(), userDAO.getId());

        if (!studentDAO.getParent().getId().equals(userDAO.getId())) {
            log.warn("Student parent mismatch. Expected: {}, Got: {}", userDAO.getId(), studentDAO.getParent().getId());
            throw new StudentParentMismatchException();
        }

        Specification<ClassAccessTokenDAO> specification =
                ClassAccessTokenSpecifications.findActiveToken();

        Optional<ClassAccessTokenDAO> token = classAccessTokenManager.findOne(specification);

        if (token.isEmpty()) {
            log.warn("No active token found");
            throw new ClassMemberIncorrectAccessCodeException();
        }

        log.info("Token from DB: '{}', Provided token: '{}'", token.get().getToken(), accessCode);
        log.info("Token expires at: {}, Current time: {}", token.get().getExpireAt(), Instant.now());

        if (!token.get().getToken().trim().equals(accessCode.trim())) {
            log.warn("Access code mismatch. Expected: '{}', Got: '{}'", token.get().getToken(), accessCode);
            throw new ClassMemberIncorrectAccessCodeException();
        }

        log.info("Access code validated successfully");

        // Check if student is already a member of this class
        if (classMemberManager.existsByClassAndStudent(token.get().getAClass(), studentDAO)) {
            log.warn("Student {} is already a member of class {}", studentId, token.get().getAClass().getId());
            throw new ClassMemberAlreadyExistsException();
        }

        ClassMemberDAO classMemberDAO = ClassMemberDAO.builder()
                .aClass(token.get().getAClass())
                .student(studentDAO)
                .joinedAt(Instant.now())
                .isConfirmed(true)
                .isArchived(false)
                .build();

        classMemberManager.saveToDatabase(classMemberDAO);
        log.info("Student {} successfully added to class {}", studentId, token.get().getId());
    }
}