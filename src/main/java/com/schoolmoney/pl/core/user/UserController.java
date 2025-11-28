package com.schoolmoney.pl.core.user;

import com.schoolmoney.pl.core.user.models.UserEditRequest;
import com.schoolmoney.pl.core.user.models.UserResponse;
import com.schoolmoney.pl.core.user.services.UserEditService;
import com.schoolmoney.pl.core.user.services.UserGetService;
import com.schoolmoney.pl.utils.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserGetService userGetService;
    private final UserEditService userEditService;

    private final static String DEFAULT_MESSAGE = "Operation successful.";

    @GetMapping(value = "/")
    @Operation(
            summary = "Get information about user",
            description = "Get information about logged user"
    )
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CustomResponse<UserResponse>> getUerDetails() {
        UserResponse serviceResponse = userGetService.getUserDetails();
        return new ResponseEntity<>(new CustomResponse<>(serviceResponse, DEFAULT_MESSAGE, HttpStatus.OK),
                                    HttpStatus.OK);
    }

    @PutMapping("/")
    @Operation(
            summary = "Edit user",
            description = "Edit user"
    )
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CustomResponse<Void>> editUser(
            @RequestBody @Valid UserEditRequest request
    ) {
        userEditService.editUser(request);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_MESSAGE, HttpStatus.OK),
                HttpStatus.OK);
    }
}
