package com.schoolmoney.pl.modules.classAccessToken;

import com.schoolmoney.pl.modules.classAccessToken.models.ClassAccessTokenResponse;
import com.schoolmoney.pl.modules.classAccessToken.service.ClassAccessTokenGetService;
import com.schoolmoney.pl.utils.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/class-access-token")
@RequiredArgsConstructor
public class ClassAccessTokenController {
    private final ClassAccessTokenGetService classAccessTokenGetService;

    private final static String DEFAULT_MESSAGE = "Operation successful.";

    @GetMapping("/{classId}")
    @Operation(
            description = "Get class access token",
            summary = "Get class access token"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<ClassAccessTokenResponse>> getClassAccessToken(
            @PathVariable UUID classId
    ) {
        ClassAccessTokenResponse response = classAccessTokenGetService.getClassAccessToken(classId);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_MESSAGE, HttpStatus.OK),
                HttpStatus.OK);
    }

}
