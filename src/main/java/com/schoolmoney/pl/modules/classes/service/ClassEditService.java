package com.schoolmoney.pl.modules.classes.service;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.classes.management.ClassManager;
import com.schoolmoney.pl.modules.classes.management.ClassNotFoundException;
import com.schoolmoney.pl.modules.classes.management.ClassParentNotInClassException;
import com.schoolmoney.pl.modules.classes.management.ClassTreasurerMismatchException;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import com.schoolmoney.pl.modules.classes.models.ClassEditRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClassEditService {
    private final ClassManager classManager;
    private final HttpServletRequest request;

    public void editClass(UUID classId, ClassEditRequest classEditRequest){
        UserDAO userDAO = (UserDAO) request.getAttribute("user");

        ClassDAO classDAO = classManager.findById(classId)
                .orElseThrow(ClassNotFoundException::new);

        if (!classDAO.getTreasurer().equals(userDAO)){
            throw new ClassTreasurerMismatchException();
        }

        List<UserDAO> parentList = classManager.getParentsFromClass(classId);
        if (parentList.contains(userDAO)){
            classDAO.setTreasurer(userDAO);
        } else {
            throw new ClassParentNotInClassException();
        }

        classDAO.setName(classEditRequest.name());
        classManager.saveToDatabase(classDAO);
    }
}
