package com.example.taskmanagementsystem.auth.JWT;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Commence the authentication process.
     *
     * This method is called when an unauthenticated user attempts to access a secure resource.
     * It sends an HTTP 401 Unauthorized response to the client.
     *
     * @param request the HTTP servlet request
     * @param response the HTTP servlet response
     * @param e the authentication exception that occurred
     * @throws IOException if an I/O exception occurs
     * @throws ServletException if a servlet exception occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}

