package com.digitallending.apigatewayservice.exceptionhandler;

import com.digitallending.apigatewayservice.dto.ApiResponse;
import com.digitallending.apigatewayservice.exception.ApiGatewayError;
import com.digitallending.apigatewayservice.exception.UnableToParseTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    static final String MESSAGE = "message";

    @ExceptionHandler(UnableToParseTokenException.class)
    public ResponseEntity<ApiResponse<ApiGatewayError>> handleUnableToParseException(Exception ex){
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, ex.getMessage());
        ApiGatewayError errorDetails = ApiGatewayError
                .builder()
                .error(errors)
                .build();

        ApiResponse<ApiGatewayError> apiResponse = ApiResponse
                .<ApiGatewayError>builder()
                .payload(errorDetails)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
