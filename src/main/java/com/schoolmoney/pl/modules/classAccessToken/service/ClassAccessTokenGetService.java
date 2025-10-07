package com.schoolmoney.pl.modules.classAccessToken.service;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.classAccessToken.management.ClassAccessTokenManager;
import com.schoolmoney.pl.modules.classAccessToken.management.ClassAccessTokenSpecifications;
import com.schoolmoney.pl.modules.classAccessToken.models.ClassAccessTokenDAO;
import com.schoolmoney.pl.modules.classAccessToken.models.ClassAccessTokenResponse;
import com.schoolmoney.pl.modules.classes.management.ClassManager;
import com.schoolmoney.pl.modules.classes.management.ClassNotFoundException;
import com.schoolmoney.pl.modules.classes.management.ClassTreasurerMismatchException;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClassAccessTokenGetService {
    private final HttpServletRequest request;
    private final ClassManager classManager;
    private final ClassAccessTokenManager classAccessTokenManager;
    private final SecureRandom secureRandom = new SecureRandom();

    public ClassAccessTokenResponse getClassAccessToken(UUID classId) {
        UserDAO userDAO = (UserDAO) request.getAttribute("user");

        ClassDAO classDAO = classManager.findById(classId)
                .orElseThrow(ClassNotFoundException::new);

        if (!classDAO.getTreasurer().getId().equals(userDAO.getId())) {
            throw new ClassTreasurerMismatchException();
        }

        Specification<ClassAccessTokenDAO> specification =
                ClassAccessTokenSpecifications.findActiveToken()
                        .and(ClassAccessTokenSpecifications.findByClass(classId));

        Optional<ClassAccessTokenDAO> existingToken = classAccessTokenManager.findOne(specification);

        if (existingToken.isPresent()) {
            log.info("Returning existing active token for class: {}", classId);
            return ClassAccessTokenResponse.builder()
                    .token(existingToken.get().getToken())
                    .build();
        } else {
            log.info("Generating new token for class: {}", classId);
            String token = generateAccessCode();

            ClassAccessTokenDAO accessTokenDAO = ClassAccessTokenDAO.builder()
                    .aClass(classDAO)
                    .token(token)
                    .expireAt(Instant.now().plusSeconds(60 * 5)) // 5 minutes
                    .isArchived(false)
                    .build();

            classAccessTokenManager.saveToDatabase(accessTokenDAO);

            return ClassAccessTokenResponse.builder()
                    .token(token)
                    .build();
        }
    }

    private String generateAccessCode() {
        int code = secureRandom.nextInt(10000);
        return String.format("%04d", code);
    }
}