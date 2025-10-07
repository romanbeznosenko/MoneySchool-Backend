package com.schoolmoney.pl.interceptor;

import com.schoolmoney.pl.core.user.management.UserManager;
import com.schoolmoney.pl.core.user.management.UserNotFoundException;
import com.schoolmoney.pl.core.user.models.UserDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserInterceptor implements HandlerInterceptor {
    private final UserManager userManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UserNotFoundException {

        log.info("UserInterceptor, handle user");
        UUID userId = (UUID) SecurityContextHolder.getContext()
                                                  .getAuthentication()
                                                  .getPrincipal();

        UserDAO user = userManager.findUserById(userId)
                                  .orElseThrow(UserNotFoundException::new);

        request.setAttribute("user", user);
        log.info("UserInterceptor, set user successfully");
        return true;
    }
}
