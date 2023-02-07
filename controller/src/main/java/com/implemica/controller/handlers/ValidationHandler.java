package com.implemica.controller.handlers;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.implemica.controller.exceptions.InvalidImageTypeException;
import com.implemica.controller.exceptions.NoSuchCarException;
import com.implemica.model.error_massage.ErrorMassageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class ValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        log.error(ex.getMessage(), (Object) ex.getStackTrace());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) ->{

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchCarException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ErrorMassageResponse> handleNoSuchCarException(NoSuchCarException ex) {

        return buildErrorResponseAndLogExceptionMassage(ex, NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorMassageResponse> handleIllegalArgumentException(IllegalArgumentException ex) {

        return buildErrorResponseAndLogExceptionMassage(ex, BAD_REQUEST);
    }

    @ExceptionHandler(InvalidImageTypeException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorMassageResponse> handleInvalidImageTypeException(InvalidImageTypeException ex) {

        return buildErrorResponseAndLogExceptionMassage(ex, BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ResponseEntity<ErrorMassageResponse> handleAuthenticationException(AuthenticationException ex) {

        return buildErrorResponseAndLogExceptionMassage(ex, UNAUTHORIZED);
    }

    @ExceptionHandler(AlgorithmMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorMassageResponse> handleAlgorithmMismatchException(AlgorithmMismatchException ex) {

        return buildErrorResponseAndLogExceptionMassage(ex, BAD_REQUEST);
    }

    @ExceptionHandler(SignatureVerificationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorMassageResponse> handleSignatureVerificationException(SignatureVerificationException ex) {

        return buildErrorResponseAndLogExceptionMassage(ex, BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(FORBIDDEN)
    public ResponseEntity<ErrorMassageResponse> handleTokenExpiredException(TokenExpiredException ex) {

        return buildErrorResponseAndLogExceptionMassage(ex, FORBIDDEN);

    }

    @ExceptionHandler(InvalidClaimException.class)
    @ResponseStatus(FORBIDDEN)
    public ResponseEntity<ErrorMassageResponse> handleInvalidClaimException(InvalidClaimException ex) {

        return buildErrorResponseAndLogExceptionMassage(ex, FORBIDDEN);
    }

    private static ResponseEntity<ErrorMassageResponse> buildErrorResponseAndLogExceptionMassage(Exception ex,
                                                                                                 HttpStatus httpStatus) {

        log.error(ex.getMessage(), (Object) ex.getStackTrace());

        ErrorMassageResponse errorMassageResponse = new ErrorMassageResponse(ex.getMessage());
        return new ResponseEntity<>(errorMassageResponse, httpStatus);
    }
}