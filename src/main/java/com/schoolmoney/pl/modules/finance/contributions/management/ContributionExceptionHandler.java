package com.schoolmoney.pl.modules.finance.contributions.management;

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
public class ContributionExceptionHandler {

    @ExceptionHandler(CollectionGoalReachedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "409", description = "Collection goal already reached or collection is archived.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                      {
                                      "data": null,
                                      "message": "Collection goal has been reached, no more payments accepted",
                                      "status": "409 CONFLICT"
                                    }
                                    """
                            )
                    )),
    })
    public ResponseEntity<CustomResponse<String>> handleCollectionGoalReachedException(CollectionGoalReachedException ex) {
        CustomResponse<String> response = new CustomResponse<>(null, ex.getMessage(), HttpStatus.CONFLICT);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ContributionExceedsLimitException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400", description = "Contribution amount exceeds allowed limit.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                      {
                                      "data": null,
                                      "message": "Contribution amount exceeds allowed limit",
                                      "status": "400 BAD_REQUEST"
                                    }
                                    """
                            )
                    )),
    })
    public ResponseEntity<CustomResponse<String>> handleContributionExceedsLimitException(ContributionExceedsLimitException ex) {
        CustomResponse<String> response = new CustomResponse<>(null, ex.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidContributionAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400", description = "Invalid contribution amount.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                      {
                                      "data": null,
                                      "message": "Invalid contribution amount",
                                      "status": "400 BAD_REQUEST"
                                    }
                                    """
                            )
                    )),
    })
    public ResponseEntity<CustomResponse<String>> handleInvalidContributionAmountException(InvalidContributionAmountException ex) {
        CustomResponse<String> response = new CustomResponse<>(null, ex.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400", description = "Insufficient balance in payer account.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                      {
                                      "data": null,
                                      "message": "Insufficient balance in account",
                                      "status": "400 BAD_REQUEST"
                                    }
                                    """
                            )
                    )),
    })
    public ResponseEntity<CustomResponse<String>> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        CustomResponse<String> response = new CustomResponse<>(null, ex.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
