package com.schoolmoney.pl.core.user.management;

import com.schoolmoney.pl.core.user.models.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserDAO, UUID> {
    Optional<UserDAO> findUserDAOByEmailAndIsArchivedFalse(String email);
}

