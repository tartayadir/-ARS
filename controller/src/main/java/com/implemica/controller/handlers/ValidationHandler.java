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

/**
 * Handles application and froms {@link ResponseEntity} with defined {@link HttpStatus} and
 * exception massage.
 */
@RestControllerAdvice
@Slf4j
public class ValidationHandler {

    /**
     * Handle validation exception to be thrown when validation on an argument annotated with @Valid fails.
     * Is thrown by {@link com.implemica.controller.controllers.CarController}.
     *
     * @param ex thrown exception
     * @return response with list of validation exception massages and HTTP status
     */
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

    /**
     * Handle exception to be thrown when cannot find car in database.
     * Is thrown by {@link com.implemica.controller.service.car.service.CarService}.
     *
     * @param ex thrown exception
     * @return response with exception massage and HTTP status
     */
    @ExceptionHandler(NoSuchCarException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ErrorMassageResponse> handleNoSuchCarException(NoSuchCarException ex) {

        return buildErrorResponseAndLogExceptionMassage(ex, NOT_FOUND);
    }

    /**
     * Handle exception to be thrown when something are invalid.
     * Is thrown by {@link com.implemica.controller.service.car.service.CarService} when car id is null.
     *
     * @param ex thrown exception
     * @return response with exception massage and HTTP status
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorMassageResponse> handleIllegalArgumentException(IllegalArgumentException ex) {

        return buildErrorResponseAndLogExceptionMassage(ex, BAD_REQUEST);
    }

    /**
     * Handle exception to be thrown when validate car image.
     * Is thrown by {@link com.implemica.controller.service.amazonS3.AmazonClient}.
     *
     * @param ex thrown exception
     * @return response with exception massage and HTTP status
     */
    @ExceptionHandler(InvalidImageTypeException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorMassageResponse> handleInvalidImageTypeException(InvalidImageTypeException ex) {

        return buildErrorResponseAndLogExceptionMassage(ex, BAD_REQUEST);
    }

    /**
     * Handle exception to be thrown when user data is invalid.
     *
     * @param ex thrown exception
     * @return response with exception massage and HTTP status
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ResponseEntity<ErrorMassageResponse> handleAuthenticationException(AuthenticationException ex) {

        return buildErrorResponseAndLogExceptionMassage(ex, UNAUTHORIZED);
    }

    /**
     * Handle exception to be thrown when provided algorithm for creating JWT token in hashing.
     *
     * @param ex thrown exception
     * @return response with exception massage and HTTP status
     */
    @ExceptionHandler(AlgorithmMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorMassageResponse> handleAlgorithmMismatchException(AlgorithmMismatchException ex) {

        return buildErrorResponseAndLogExceptionMassage(ex, BAD_REQUEST);
    }

    /**
     * Handle exception to be thrown when perform JWT token verification and signature is invalid.
     *
     * @param ex thrown exception
     * @return response with exception massage and HTTP status
     */
    @ExceptionHandler(SignatureVerificationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorMassageResponse> handleSignatureVerificationException(SignatureVerificationException ex) {

        return buildErrorResponseAndLogExceptionMassage(ex, BAD_REQUEST);
    }

    /**
     * Handle exception to be thrown when perform JWT token verification and token has expired.
     *
     * @param ex thrown exception
     * @return response with exception massage and HTTP status
     */
    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(FORBIDDEN)
    public ResponseEntity<ErrorMassageResponse> handleTokenExpiredException(TokenExpiredException ex) {

        return buildErrorResponseAndLogExceptionMassage(ex, FORBIDDEN);

    }

    /**
     * Handle exception to be thrown when perform JWT token verification and a claim contained
     * a different value than the expected one.
     *
     * @param ex thrown exception
     * @return response with exception massage and HTTP status
     */
    @ExceptionHandler(InvalidClaimException.class)
    @ResponseStatus(FORBIDDEN)
    public ResponseEntity<ErrorMassageResponse> handleInvalidClaimException(InvalidClaimException ex) {

        return buildErrorResponseAndLogExceptionMassage(ex, FORBIDDEN);
    }

    /**
     * Forms {@link ResponseEntity} with exception massage and passed HTTP status
     *
     * @param ex thrown exception
     * @param httpStatus HTTP status that will set response
     * @return response with exception massage and HTTP status
     */
    private static ResponseEntity<ErrorMassageResponse> buildErrorResponseAndLogExceptionMassage(Exception ex,
                                                                                                 HttpStatus httpStatus) {

        log.error(ex.getMessage(), (Object) ex.getStackTrace());

        ErrorMassageResponse errorMassageResponse = new ErrorMassageResponse(ex.getMessage());
        return new ResponseEntity<>(errorMassageResponse, httpStatus);
    }
}