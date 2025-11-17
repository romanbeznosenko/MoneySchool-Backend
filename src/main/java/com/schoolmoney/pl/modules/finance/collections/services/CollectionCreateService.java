package com.schoolmoney.pl.modules.finance.collections.services;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.classes.management.ClassManager;
import com.schoolmoney.pl.modules.classes.management.ClassMapper;
import com.schoolmoney.pl.modules.classes.management.ClassNotFoundException;
import com.schoolmoney.pl.modules.classes.management.ClassTreasurerMismatchException;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import com.schoolmoney.pl.modules.classes.models.Class;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionManager;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionMapper;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionDAO;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionRequest;
import com.schoolmoney.pl.modules.finance.collections.models.Collection;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollectionCreateService {
    private final HttpServletRequest request;
    private final CollectionManager collectionManager;
    private final CollectionMapper collectionMapper;
    private final ClassManager classManager;
    private final ClassMapper classMapper;

    public void create(CollectionRequest collectionRequest, UUID classId){
        log.info("Creating collection for class with id: {}", classId);

        UserDAO userDAO = (UserDAO)request.getAttribute("user");
        ClassDAO classDAO = classManager.findById(classId)
                .orElseThrow(ClassNotFoundException::new);
        if (!classDAO.getTreasurer().equals(userDAO)) {
            throw new ClassTreasurerMismatchException();
        }
        Class aClass = classMapper.mapToDomain(classDAO, new CycleAvoidingMappingContext());

        Collection collection = CollectionBuilders.buildCollectionFromRequest(collectionRequest, aClass);
        CollectionDAO collectionDAO = collectionMapper.mapToEntity(collection, new CycleAvoidingMappingContext());

        collectionManager.saveToDatabase(collectionDAO);

        log.info("Successfully created collection for class with id: {}", classId);
    }
}
