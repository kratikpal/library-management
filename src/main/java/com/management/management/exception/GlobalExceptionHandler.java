package com.management.management.exception;

import com.management.management.Constants.HttpConstants;
import com.management.management.utility.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<?>> handleException(Exception ex) {
        return new ResponseEntity<>(
                new GenericResponse<>(
                        HttpConstants.FAILURE,
                        ex.getMessage(),
                        null),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<GenericResponse<?>> handleHttpException(HttpException ex) {
        return new ResponseEntity<>(
                new GenericResponse<>(
                        ex.getStatusCode(),
                        ex.getMessage(),
                        null),
                ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(
                new GenericResponse<>(
                        HttpConstants.FAILURE,
                        "Validation failed",
                        errors),
                HttpStatus.BAD_REQUEST);
    }
}
