package com.example.taskmanagementsystem.exceptions.jwt;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * JwtAuthenticationExceptionHandler is a class that handles exceptions related to JWT authentication.
 * It is a controller advice class that extends ResponseEntityExceptionHandler, allowing it to handle exceptions globally for all controllers.
 */
@ControllerAdvice
public class JwtAuthenticationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(JwtAuthenticationException.class)
    protected ResponseEntity<Object> handleJwtAuthenticationException(JwtAuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
