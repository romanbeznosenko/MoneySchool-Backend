package com.schoolmoney.pl.modules.classes.service;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.classes.management.ClassManager;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClassCreateService {
    private final ClassManager classManager;
    private final HttpServletRequest request;

    public void createClass(String name){
        UserDAO userDAO = (UserDAO) request.getAttribute("user");

        ClassDAO classDAO = ClassDAO.builder()
                .treasurer(userDAO)
                .name(name)
                .isArchived(false)
                .build();

        classManager.saveToDatabase(classDAO);
    }
}
