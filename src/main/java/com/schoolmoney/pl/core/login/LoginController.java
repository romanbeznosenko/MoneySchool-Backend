package com.schoolmoney.pl.core.login;

import com.schoolmoney.pl.core.login.management.AccountNotActivatedException;
import com.schoolmoney.pl.core.login.management.IncorrectLoginCredentialsException;
import com.schoolmoney.pl.core.login.models.LoginRequest;
import com.schoolmoney.pl.core.login.services.LoginService;
import com.schoolmoney.pl.core.user.management.UserNotFoundException;
import com.schoolmoney.pl.utils.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping(value = "/auth/login")
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "Login user",
            description = "Login user with provided username and password")
    public ResponseEntity<CustomResponse<Object>> loginUser(
            @Valid
            @RequestBody LoginRequest request) throws UserNotFoundException, IncorrectLoginCredentialsException, AccountNotActivatedException {
        loginService.loginUser(request);

        return new ResponseEntity<>(new CustomResponse<>(null, "Login successful.", HttpStatus.OK),
                                    HttpStatus.OK);
    }
}


