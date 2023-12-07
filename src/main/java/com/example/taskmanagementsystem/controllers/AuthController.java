package com.example.taskmanagementsystem.controllers;

import com.example.taskmanagementsystem.auth.JWT.JwtUserDetailsService;
import com.example.taskmanagementsystem.exceptions.jwt.JwtAuthenticationException;
import com.example.taskmanagementsystem.models.DTO.UserRegisterDTO;
import com.example.taskmanagementsystem.models.User;
import com.example.taskmanagementsystem.requestsAndResponses.AuthRequest;
import com.example.taskmanagementsystem.requestsAndResponses.AuthResponse;
import com.example.taskmanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController is responsible for handling authentication and registration requests.
 * It provides endpoints for login and user registration.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private UserService userService;

    public AuthController(JwtUserDetailsService userDetailsService, UserService userService) {
        this.jwtUserDetailsService = userDetailsService;
        this.userService = userService;
    }

    /**
     * Creates an authentication token for the given authentication request.
     *
     * @param authRequest the authentication request object containing the email and password
     * @return a ResponseEntity with a successful authentication response containing the token
     * @throws JwtAuthenticationException if there is an error during authentication and token generation
     */
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws JwtAuthenticationException {
        String token = jwtUserDetailsService.authenticateAndGenerateToken(authRequest.getEmail(), authRequest.getPassword());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * Registers a new user with the given information.
     *
     * @param user the user registration request object containing the user details
     * @return a ResponseEntity with a success message containing the registered user ID
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegisterDTO user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully. User ID: " + registeredUser.getId());
    }
}

