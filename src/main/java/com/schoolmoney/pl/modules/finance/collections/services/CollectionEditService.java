package com.schoolmoney.pl.modules.finance.collections.services;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.classes.management.ClassTreasurerMismatchException;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionManager;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionNotFoundException;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionDAO;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class CollectionEditService {
    private final HttpServletRequest request;
    private final CollectionManager collectionManager;

    public void edit(CollectionRequest collectionRequest, UUID collectionId){
        log.info("editing collection with id {}",collectionId);

        UserDAO userDAO = (UserDAO)request.getAttribute("user");
        CollectionDAO collectionDAO = collectionManager.findById(collectionId)
                .orElseThrow(CollectionNotFoundException::new);

        if (!collectionDAO.getAClass().getTreasurer().equals(userDAO)){
            throw new ClassTreasurerMismatchException();
        }

        collectionDAO.setTitle(collectionRequest.title());
        collectionDAO.setDescription(collectionRequest.description());
        collectionManager.saveToDatabase(collectionDAO);

        log.info("Edit collection with id {} finished successfully!",collectionId);
    }
}
