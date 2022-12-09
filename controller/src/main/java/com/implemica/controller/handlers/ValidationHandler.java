package com.implemica.controller.handlers;

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

@RestControllerAdvice
@Slf4j
public class ValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        log.error(ex.getMessage(), (Object) ex.getStackTrace());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) ->{

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchCarException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMassageResponse> handleNoSuchCarException(NoSuchCarException ex) {

        log.error(ex.getMessage(), (Object) ex.getStackTrace());

        ErrorMassageResponse errorMassageResponse = new ErrorMassageResponse(ex.getMessage());
        return new ResponseEntity<>(errorMassageResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMassageResponse> handleIllegalArgumentException(IllegalArgumentException ex) {

        log.error(ex.getMessage(), (Object) ex.getStackTrace());

        ErrorMassageResponse errorMassageResponse = new ErrorMassageResponse(ex.getMessage());
        return new ResponseEntity<>(errorMassageResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidImageTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMassageResponse> handleInvalidImageTypeException(InvalidImageTypeException ex) {

        log.error(ex.getMessage(), (Object) ex.getStackTrace());

        ErrorMassageResponse errorMassageResponse = new ErrorMassageResponse(ex.getMessage());
        return new ResponseEntity<>(errorMassageResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Void> handleAuthenticationException(AuthenticationException ex) {

        log.error(ex.getMessage(), (Object) ex.getStackTrace());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}