package com.schoolmoney.pl.modules.finance.financeAccount.management;

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
public class FinanceAccountExceptionHandler {
    @ExceptionHandler(FinanceAccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404", description = "Finance account not found in the system.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                      {
                                      "data": null,
                                      "message": "Finance account found in the system.",
                                      "status": "404 NOT_FOUND"
                                    }
                                    """
                            )
                    )),
    })
    public ResponseEntity<CustomResponse<String>> handleFinanceAccountNotFoundException(FinanceAccountNotFoundException ex) {
        CustomResponse<String> response = new CustomResponse<>(null, ex.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FinanceAccountAlreadyExistingException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "403", description = "User already have finance account.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                      {
                                      "data": null,
                                      "message": "User already have finance account",
                                      "status": "403 FORBIDDEN"
                                    }
                                    """
                            )
                    )),
    })
    public ResponseEntity<CustomResponse<String>> handleFinanceAccountAlreadyExisting(FinanceAccountAlreadyExistingException ex) {
        CustomResponse<String> response = new CustomResponse<>(null, ex.getMessage(), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(FinanceAccountForbiddenCreationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "403", description = "User can not create a new finance account.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                      {
                                      "data": null,
                                      "message": "User can not create a new finance account",
                                      "status": "403 FORBIDDEN"
                                    }
                                    """
                            )
                    )),
    })
    public ResponseEntity<CustomResponse<String>> handleFinanceAccountForbiddenCreationException(FinanceAccountForbiddenCreationException ex) {
        CustomResponse<String> response = new CustomResponse<>(null, ex.getMessage(), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(FinanceAccountOwnerMismatchException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "403", description = "User is not the owner of this finance account.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                      {
                                      "data": null,
                                      "message": "User is not the owner of this finance account",
                                      "status": "403 FORBIDDEN"
                                    }
                                    """
                            )
                    )),
    })
    public ResponseEntity<CustomResponse<String>> handleFinanceAccountOwnerMismatchException(FinanceAccountOwnerMismatchException ex) {
        CustomResponse<String> response = new CustomResponse<>(null, ex.getMessage(), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
