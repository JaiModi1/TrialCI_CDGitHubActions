package com.digitallending.userservice.exceptionhandler;

import com.digitallending.userservice.exception.AadhaarPANLinkException;
import com.digitallending.userservice.exception.AttributeValueNotFoundException;
import com.digitallending.userservice.exception.BankAccountNotFoundException;
import com.digitallending.userservice.exception.BusinessTypeNotExistsException;
import com.digitallending.userservice.exception.DetailsNotFoundException;
import com.digitallending.userservice.exception.DocumentTypeNotFoundException;
import com.digitallending.userservice.exception.EmailAlreadyExistsException;
import com.digitallending.userservice.exception.EmptyDocumentException;
import com.digitallending.userservice.exception.IncorrectCredentialsException;
import com.digitallending.userservice.exception.InvalidMobileNumberException;
import com.digitallending.userservice.exception.InvalidUserException;
import com.digitallending.userservice.exception.MailNotSentException;
import com.digitallending.userservice.exception.PANInformationVerifyException;
import com.digitallending.userservice.exception.PreviousStepsNotDoneException;
import com.digitallending.userservice.exception.UnAuthorizedException;
import com.digitallending.userservice.exception.UpdateException;
import com.digitallending.userservice.exception.UserNotFoundException;
import com.digitallending.userservice.exception.WrongAadhaarNoException;
import com.digitallending.userservice.exception.WrongOTPException;
import com.digitallending.userservice.model.dto.apiresponse.APIResponseDTO;
import com.digitallending.userservice.model.dto.apiresponse.UserServiceError;
import jakarta.ws.rs.BadRequestException;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    static final String MESSAGE = "message";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponseDTO<UserServiceError>> internalServerExceptionHandler(Exception exception, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, exception.getMessage());

        APIResponseDTO<UserServiceError> apiResponseDTO = APIResponseDTO
                .<UserServiceError>builder()
                .payload(new UserServiceError(errors, request.getDescription(false)))
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler({WrongOTPException.class,
            IncorrectCredentialsException.class,
            MailNotSentException.class,
            AadhaarPANLinkException.class,
            InvalidMobileNumberException.class,
            PANInformationVerifyException.class,
            EmptyDocumentException.class,
            InvalidUserException.class,
            PreviousStepsNotDoneException.class,
            UpdateException.class,
            WrongAadhaarNoException.class,
            EmailAlreadyExistsException.class
    })
    public ResponseEntity<APIResponseDTO<UserServiceError>> badRequestExceptionHandler(Exception exception, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, exception.getMessage());

        APIResponseDTO<UserServiceError> apiResponseDTO = APIResponseDTO
                .<UserServiceError>builder()
                .payload(new UserServiceError(errors, request.getDescription(false)))
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({UserNotFoundException.class,
            DetailsNotFoundException.class,
            BankAccountNotFoundException.class,
            DocumentTypeNotFoundException.class,
            BusinessTypeNotExistsException.class,
            AttributeValueNotFoundException.class
    })
    public ResponseEntity<APIResponseDTO<UserServiceError>> resourceNotFoundException(Exception exception, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, exception.getMessage());

        APIResponseDTO<UserServiceError> apiResponseDTO = APIResponseDTO
                .<UserServiceError>builder()
                .payload(new UserServiceError(errors, request.getDescription(false)))
                .build();
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<APIResponseDTO<UserServiceError>> handleBadRequestException(Exception ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, ex.getMessage());

        UserServiceError errorDetails = new UserServiceError(errors, request.getDescription(false));
        APIResponseDTO<UserServiceError> apiResponse = APIResponseDTO
                .<UserServiceError>builder()
                .payload(errorDetails)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<APIResponseDTO<UserServiceError>> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        String errorMessage = "Data integrity violation.";

        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, errorMessage);

        UserServiceError errorDetails = new UserServiceError(errors, request.getDescription(false));
        APIResponseDTO<UserServiceError> apiResponse = APIResponseDTO
                .<UserServiceError>builder()
                .payload(errorDetails)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatusCode status,
            @NotNull WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        UserServiceError errorDetails = new UserServiceError(
                errors,
                request.getDescription(false)
        );
        APIResponseDTO<UserServiceError> apiResponse = APIResponseDTO
                .<UserServiceError>builder()
                .payload(errorDetails)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<APIResponseDTO<UserServiceError>> handleUnAuthorizedException(Exception ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, ex.getMessage());

        UserServiceError errorDetails = new UserServiceError(errors, request.getDescription(false));
        APIResponseDTO<UserServiceError> apiResponse = APIResponseDTO
                .<UserServiceError>builder()
                .payload(errorDetails)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

}
















