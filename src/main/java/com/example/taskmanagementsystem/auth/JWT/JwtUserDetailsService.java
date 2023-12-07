package com.example.taskmanagementsystem.auth.JWT;

import com.example.taskmanagementsystem.exceptions.jwt.JwtAuthenticationException;
import com.example.taskmanagementsystem.models.SecurityUser;
import com.example.taskmanagementsystem.models.User;
import com.example.taskmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This class is responsible for handling user authentication and generating JWT token.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Retrieves the UserDetails of a user by their email.
     *
     * @param email the email of the user to retrieve UserDetails for
     * @return the UserDetails of the specified user
     * @throws UsernameNotFoundException if the user with the given email is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("user" + email + "" +
                "not found"));
        return SecurityUser.fromUser(user);
    }

    /**
     * Authenticates the user with the given email and password and generates a JWT token.
     *
     * @param email    the email of the user to authenticate
     * @param password the password of the user to authenticate
     * @return the generated JWT token
     * @throws JwtAuthenticationException if authentication fails
     */
    public String authenticateAndGenerateToken(String email, String password) throws JwtAuthenticationException {
        Authentication authenticate = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                email, password
                        )
                );
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserDetails userDetails = loadUserByUsername(email);
        return jwtTokenUtil.generateToken(userDetails.getUsername());
    }

    /**
     * Authenticates the user with the given email and password.
     *
     * @param email    the email of the user to authenticate
     * @param password the password of the user to authenticate
     * @return the authenticated user
     * @throws JwtAuthenticationException if authentication fails due to invalid credentials
     */
    private Authentication authenticate(String email, String password) throws JwtAuthenticationException {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            throw new JwtAuthenticationException("Invalid credentials");
        }
    }
}

