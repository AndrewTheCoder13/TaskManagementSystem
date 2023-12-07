package com.example.taskmanagementsystem.exceptions.access;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;

/**
 * This class is a global exception handler for handling AccessDeniedException in the application.
 * It extends the ResponseEntityExceptionHandler class provided by Spring Framework.
 *
 * Usage:
 * Add this class with @ControllerAdvice annotation to handle AccessDeniedException globally.
 * When an AccessDeniedException is thrown in any controller method, this handler will catch it and return a
 * ResponseEntity with HTTP status code 403 (FORBIDDEN) and the error message from the exception.
 */
@ControllerAdvice
public class AccessDeniedExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
