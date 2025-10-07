package com.schoolmoney.pl.core.user.services;

import com.schoolmoney.pl.core.user.management.UserMapper;
import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.core.user.models.UserResponse;
import com.schoolmoney.pl.files.storage.services.StorageService;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserGetService {
    private final HttpServletRequest request;
    private final UserMapper userMapper;
    private final StorageService storageService;

    public UserResponse getUserDetails() {
        log.info("Fetching user details");

        UserDAO user = (UserDAO) request.getAttribute("user");

        return userMapper.mapToResponseFromEntity(user, new CycleAvoidingMappingContext(), storageService);
    }
}
