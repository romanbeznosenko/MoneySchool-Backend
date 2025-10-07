package com.schoolmoney.pl.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Schema(description = "Wrapper class for custom API responses, containing data, message, and HTTP status.")
public class CustomResponse<T> {
    @Schema(description = "The actual response data.")
    private T data;

    @Schema(description = "A message providing additional details about the response.", example = "Operation successful")
    private String message;


    @Schema(description = "The HTTP status of the response.", example = "200 OK/201 CREATED")
    private HttpStatus httpStatus;
}