package com.example.taskmanagementsystem;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.taskmanagementsystem.auth.JWT.JwtUserDetailsService;
import com.example.taskmanagementsystem.controllers.AuthController;
import com.example.taskmanagementsystem.exceptions.jwt.JwtAuthenticationException;
import com.example.taskmanagementsystem.models.DTO.UserRegisterDTO;
import com.example.taskmanagementsystem.models.User;
import com.example.taskmanagementsystem.requestsAndResponses.AuthRequest;
import com.example.taskmanagementsystem.requestsAndResponses.AuthResponse;
import com.example.taskmanagementsystem.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class AuthControllerTest {

    private AuthController authController;
    private JwtUserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(JwtUserDetailsService.class);
        userService = mock(UserService.class);
        authController = new AuthController(userDetailsService, userService);
    }

    @Test
    void whenCreateAuthenticationToken_thenReturnsToken() throws JwtAuthenticationException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String token = "testToken";
        when(userDetailsService.authenticateAndGenerateToken(any(), any())).thenReturn(token);

        AuthRequest authRequest = new AuthRequest("email@example.com", "password");
        ResponseEntity<?> responseEntity = authController.createAuthenticationToken(authRequest);

        assertEquals(token, ((AuthResponse)responseEntity.getBody()).getToken());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void whenRegisterUser_thenReturnsSuccessMessage() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        User user = new User();
        user.setId(1L);
        when(userService.registerUser(any())).thenReturn(user);

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        ResponseEntity<String> responseEntity = authController.registerUser(userRegisterDTO);

        assertEquals("User registered successfully. User ID: " + user.getId(), responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
