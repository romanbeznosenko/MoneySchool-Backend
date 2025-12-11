package com.schoolmoney.pl.utils;

import com.schoolmoney.pl.core.authAccount.management.AuthAccountManager;
import com.schoolmoney.pl.core.authAccount.models.AuthAccountDAO;
import com.schoolmoney.pl.core.student.management.StudentManager;
import com.schoolmoney.pl.core.student.models.StudentDAO;
import com.schoolmoney.pl.core.user.management.UserManager;
import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.classMember.management.ClassMemberManager;
import com.schoolmoney.pl.modules.classMember.models.ClassMemberDAO;
import com.schoolmoney.pl.modules.classes.management.ClassManager;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountManager;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountType;
import com.schoolmoney.pl.utils.enums.AuthTypeEnum;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class InitializeDatabase {
    private final UserManager userManager;
    private final PasswordEncoder passwordEncoder;
    private final AuthAccountManager authAccountManager;
    private final ClassManager classManager;
    private final StudentManager studentManager;
    private final ClassMemberManager classMemberManager;
    private final FinanceAccountManager financeAccountManager;

    @PostConstruct
    public void initializeDatabase(){
        if (userManager.findUserByEmailAndArchivedFalse("admin@schoolmoney.local").isEmpty()){
            log.info("Creating new user");
            createUser();
        } else {
            log.info("User already exists");
        }
    }

    private void createUser(){
        // Create Admin User (Treasurer)
        UserDAO userDAO = UserDAO.builder()
                .email("admin@schoolmoney.local")
                .name("Admin")
                .surname("Adminowski")
                .isArchived(false)
                .build();

        userDAO = userManager.saveToDatabase(userDAO);

        AuthAccountDAO authAccountDAO = AuthAccountDAO.builder()
                .authType(AuthTypeEnum.EMAIL)
                .email("admin@schoolmoney.local")
                .password(passwordEncoder.encode("admin"))
                .userId(userDAO.getId())
                .isActivated(true)
                .build();

        authAccountManager.saveToDatabase(authAccountDAO);

        FinanceAccountDAO financeAccountDAO = FinanceAccountDAO.builder()
                .IBAN("PL61109010140000071219812874")
                .owner(userDAO)
                .accountType(FinanceAccountType.USER)
                .balance(5000.0)
                .isTreasurerAccount(false)
                .isArchived(false)
                .build();

        financeAccountManager.saveToDatabase(financeAccountDAO);

        // Create Test User (Parent)
        UserDAO userDAO1 = UserDAO.builder()
                .email("test@schoolmoney.local")
                .name("Test")
                .surname("Testowski")
                .isArchived(false)
                .build();

        userDAO1 = userManager.saveToDatabase(userDAO1);

        AuthAccountDAO authAccountDAO1 = AuthAccountDAO.builder()
                .authType(AuthTypeEnum.EMAIL)
                .email("test@schoolmoney.local")
                .password(passwordEncoder.encode("admin"))
                .userId(userDAO1.getId())
                .isActivated(true)
                .build();

        authAccountManager.saveToDatabase(authAccountDAO1);

        // Create Class
        ClassDAO classDAO = ClassDAO.builder()
                .treasurer(userDAO)
                .name("4B")
                .isArchived(false)
                .build();

        classDAO = classManager.saveToDatabase(classDAO);

        // Create Student for Admin
        StudentDAO studentDAO = StudentDAO.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .birthDate(LocalDate.of(2010, 5, 15))
                .parent(userDAO)
                .isArchived(false)
                .build();

        studentDAO = studentManager.saveToDatabase(studentDAO);

        // Add Student to Class
        ClassMemberDAO classMemberDAO = ClassMemberDAO.builder()
                .aClass(classDAO)
                .student(studentDAO)
                .isConfirmed(true)
                .joinedAt(Instant.now())
                .isArchived(false)
                .build();

        classMemberManager.saveToDatabase(classMemberDAO);

        log.info("Successfully created admin user, test user, class '{}', student '{}', and added student to class",
                classDAO.getName(),
                studentDAO.getFirstName() + " " + studentDAO.getLastName());
    }
}