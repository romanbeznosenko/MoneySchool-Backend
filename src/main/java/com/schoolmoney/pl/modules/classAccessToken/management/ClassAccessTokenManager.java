package com.schoolmoney.pl.modules.classAccessToken.management;

import com.schoolmoney.pl.modules.classAccessToken.models.ClassAccessTokenDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassAccessTokenManager {
    private final ClassAccessTokenRepository classAccessTokenRepository;

    public ClassAccessTokenDAO saveToDatabase(ClassAccessTokenDAO classAccessTokenDAO) {
        return classAccessTokenRepository.save(classAccessTokenDAO);
    }

    public Optional<ClassAccessTokenDAO> findOne(Specification<ClassAccessTokenDAO> specification) {
        return classAccessTokenRepository.findOne(specification);
    }
}