package com.example.taskmanagementsystem.auth.JWT;


import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

/**
 * JwtRequestFilter is a filter that intercepts incoming requests and processes JWT authentication.
 *
 * The filter extracts the JWT token from the "Authorization" header of the request,
 * and validates and loads the user details using the JwtTokenUtil and JwtUserDetailsService.
 *
 * If the JWT token is valid and the user details are loaded successfully, the filter
 * creates an authentication object and sets it in the SecurityContextHolder, allowing
 * the user to access protected resources.
 *
 * This class is annotated with @Component to be automatically detected and registered
 * as a bean in the application context.
 *
 * This class extends the OncePerRequestFilter class, ensuring that the doFilterInternal
 * method is only called once per request.
 *
 * This class has autowired dependencies for JwtUserDetailsService and JwtTokenUtil.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Overrides the doFilterInternal method from the AbstractAuthenticationProcessingFilter class.
     * This method is responsible for filtering HTTP requests and responses, and handling authentication.
     *
     * @param request The HttpServletRequest object representing the incoming HTTP request.
     * @param response The HttpServletResponse object representing the outgoing HTTP response.
     * @param chain The FilterChain object used to invoke the next filter in the chain.
     * @throws ServletException If the processing of the request encounters a servlet exception.
     * @throws IOException If an I/O error occurs during the processing of the request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");

        String jwtToken = getToken(requestTokenHeader);
        String username = getUsername(jwtToken);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            validateAndSetAuthentication(jwtToken, username, request);
        }

        chain.doFilter(request, response);
    }

    /**
     * Retrieves the token from the Authorization header of the HTTP request.
     *
     * @param requestTokenHeader The Authorization header of the HTTP request.
     * @return The extracted token if the Authorization header is present and starts with "Bearer ", otherwise null.
     */
    private String getToken(String requestTokenHeader){
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            return requestTokenHeader.substring(7);
        }
        return null;
    }

    /**
     * Retrieves the username from the given JWT token.
     *
     * @param jwtToken The JWT token from which to extract the username.
     * @return The username extracted from the JWT token if the token is valid and has a username claim, otherwise null.
     */
    private String getUsername(String jwtToken){
        try {
            return jwtTokenUtil.getUsernameFromToken(jwtToken);
        } catch (IllegalArgumentException e) {
            logger.error("JWT Token error: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("JWT Token has expired", e);
        }
        return null;
    }

    /**
     * Validates the JWT token and sets the authentication in the security context.
     *
     * @param jwtToken The JWT token to validate.
     * @param username The username associated with the JWT token.
     * @param request The HTTP request in which the authentication is being set.
     */
    private void validateAndSetAuthentication(String jwtToken, String username, HttpServletRequest request){
        UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }
}
