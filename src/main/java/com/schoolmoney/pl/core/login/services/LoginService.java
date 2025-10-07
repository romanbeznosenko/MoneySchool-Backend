package com.schoolmoney.pl.core.login.services;

import com.schoolmoney.pl.core.authAccount.management.AuthAccountManager;
import com.schoolmoney.pl.core.authAccount.management.AuthAccountMapper;
import com.schoolmoney.pl.core.authAccount.models.AuthAccount;
import com.schoolmoney.pl.core.authAccount.models.AuthAccountDAO;
import com.schoolmoney.pl.core.login.management.AccountNotActivatedException;
import com.schoolmoney.pl.core.login.management.IncorrectLoginCredentialsException;
import com.schoolmoney.pl.core.login.models.LoginRequest;
import com.schoolmoney.pl.core.user.management.UserNotFoundException;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import com.schoolmoney.pl.utils.enums.AuthTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final HttpServletRequest httpServletRequest;
    private final PasswordEncoder passwordEncoder;
    private final AuthAccountMapper authAccountMapper;
    private final AuthAccountManager authAccountManager;

    public void loginUser(LoginRequest request) throws UserNotFoundException, BadCredentialsException, IncorrectLoginCredentialsException, AccountNotActivatedException {
        log.info("Authorizing user: {}", request.email());
        String trimmedEmail = request.email()
                                     .toLowerCase()
                                     .strip();
        String trimmedPassword = request.password()
                                        .strip();
        AuthAccountDAO user = authAccountManager.findByEmailAndAuthTypeAndIsActivatedTrue(trimmedEmail,
                                                                                          AuthTypeEnum.EMAIL)
                                                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        AuthAccount authAccount = authAccountMapper.mapToDomain(user, new CycleAvoidingMappingContext());
        if (!authAccount.getIsActivated()) throw new AccountNotActivatedException("User not activated!");
        if (!passwordEncoder.matches(trimmedPassword, authAccount.getPassword()))
            throw new IncorrectLoginCredentialsException("Incorrect login credentials!");
        log.info("Authorized user: {}", request.email());

        HttpSession httpSession = httpServletRequest.getSession(true);
        if (!httpSession.isNew()) {
            log.info("Invalidating existing session for user: {}", authAccount.getAuthAccountId()
                                                                              .getId());
            httpSession.invalidate();
            log.info("Creating new session for user: {}", authAccount.getAuthAccountId()
                                                                     .getId());
            httpSession = httpServletRequest.getSession(true);
        }

        httpSession.setAttribute("auth_id", authAccount.getAuthAccountId()
                                                       .getId());
        httpSession.setAttribute("user_id", authAccount.getUserId());

        PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(
                authAccount.getUserId(), null, new ArrayList<>());

        SecurityContextHolder.getContext()
                             .setAuthentication(authenticationToken);

        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                                 SecurityContextHolder.getContext());

        if (Objects.equals(request.staySignedIn(), true)) {
            log.info("Extended session to 7 days for user : {}", authAccount.getAuthAccountId()
                                                                            .getId());

            httpSession.setMaxInactiveInterval(httpSession.getMaxInactiveInterval() * 24 * 7);
            httpSession.setAttribute("stay_signed_in", true);
        } else {
            httpSession.setAttribute("stay_signed_in", false);
        }
    }
}

