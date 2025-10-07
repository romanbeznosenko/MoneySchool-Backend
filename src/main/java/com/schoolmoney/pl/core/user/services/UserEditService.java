package com.schoolmoney.pl.core.user.services;

import com.schoolmoney.pl.core.user.management.UserManager;
import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.core.user.models.UserEditRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserEditService {
    private final UserManager userManager;
    private final HttpServletRequest request;

    public void editUser(UserEditRequest userEditRequest){
        UserDAO userDAO = (UserDAO) request.getAttribute("user");

        userDAO.setName(userEditRequest.name());
        userDAO.setSurname(userEditRequest.surname());

        userManager.saveToDatabase(userDAO);
    }
}
