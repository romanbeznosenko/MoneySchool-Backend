package com.schoolmoney.pl.modules.classMember.management;

import com.schoolmoney.pl.modules.classes.management.ClassParentNotInClassException;
import com.schoolmoney.pl.utils.CustomResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ClassMemberExceptionHandler {
    @ExceptionHandler(ClassMemberIncorrectAccessCodeException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "403",
                    description = "Access code is incorrect.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                {
                                    "data": null,
                                    "message": "Access code is incorrect.",
                                    "status": "403 FORBIDDEN"
                                }
                                """
                            )
                    )
            )
    })
    public ResponseEntity<CustomResponse<String>> handleClassMemberIncorrectAccessCodeException(ClassMemberIncorrectAccessCodeException ex) {
        CustomResponse<String> response = new CustomResponse<>(null, ex.getMessage(), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ClassMemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Class Member Not Found",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                {
                                    "data": null,
                                    "message": "Class Member Not Found",
                                    "status": "404 NOT_FOUND"
                                }
                                """
                            )
                    )
            )
    })
    public ResponseEntity<CustomResponse<String>> handleClassMemberNotFoundException(ClassMemberNotFoundException ex) {
        CustomResponse<String> response = new CustomResponse<>(null, ex.getMessage(), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ClassMemberAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "409",
                    description = "Student is already a member of this class.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                {
                                    "data": null,
                                    "message": "Student is already a member of this class.",
                                    "status": "409 CONFLICT"
                                }
                                """
                            )
                    )
            )
    })
    public ResponseEntity<CustomResponse<String>> handleClassMemberAlreadyExistsException(ClassMemberAlreadyExistsException ex) {
        CustomResponse<String> response = new CustomResponse<>(null, ex.getMessage(), HttpStatus.CONFLICT);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
