package com.schoolmoney.pl.modules.classAccessToken.management;

import com.schoolmoney.pl.modules.classAccessToken.models.ClassAccessTokenDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ClassAccessTokenRepository extends
        JpaRepository<ClassAccessTokenDAO, UUID>,
        JpaSpecificationExecutor<ClassAccessTokenDAO> {
}