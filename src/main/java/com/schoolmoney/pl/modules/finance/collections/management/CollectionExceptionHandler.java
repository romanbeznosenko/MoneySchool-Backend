package com.schoolmoney.pl.modules.finance.collections.management;

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
public class CollectionExceptionHandler {
    @ExceptionHandler(CollectionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404", description = "Collection not found in the system.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                      {
                                      "data": null,
                                      "message": "Collection not found in the system.",
                                      "status": "404 NOT_FOUND"
                                    }
                                    """
                            )
                    )),
    })
    public ResponseEntity<CustomResponse<String>> handleCollectionNotFoundException(CollectionNotFoundException ex) {
        CustomResponse<String> response = new CustomResponse<>(null, ex.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
