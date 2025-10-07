package com.schoolmoney.pl.core.user.management;

import com.schoolmoney.pl.core.user.models.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManager {
    private final UserRepository userRepository;

    public UserDAO saveToDatabase(UserDAO userDAO) {
        return userRepository.save(userDAO);
    }

    public Optional<UserDAO> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<UserDAO> findUserByEmailAndArchivedFalse(String email) {
        return userRepository.findUserDAOByEmailAndIsArchivedFalse(email);
    }
}
