package com.schoolmoney.pl.modules.classes.service;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.classes.management.ClassManager;
import com.schoolmoney.pl.modules.classes.management.ClassNotFoundException;
import com.schoolmoney.pl.modules.classes.management.ClassTreasurerMismatchException;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClassDeleteService {
    private final HttpServletRequest request;
    private final ClassManager classManager;

    public void deleteClass(UUID classId){
        UserDAO userDAO = (UserDAO) request.getAttribute("user");

        ClassDAO classDAO = classManager.findById(classId)
                .orElseThrow(ClassNotFoundException::new);

        if (!classDAO.getTreasurer().getId().equals(userDAO.getId())) {
            throw new ClassTreasurerMismatchException();
        }

        classManager.delete(classDAO);
    }
}
