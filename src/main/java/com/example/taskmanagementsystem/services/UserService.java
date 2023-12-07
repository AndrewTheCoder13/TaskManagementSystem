package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.models.DTO.UserRegisterDTO;
import com.example.taskmanagementsystem.models.User;
import com.example.taskmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user registration.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new user with the given user information.
     *
     * @param userDTO the UserRegisterDTO object containing the user information
     * @return the registered User object
     * @throws RuntimeException if the username (email) is already taken
     */
    public User registerUser(UserRegisterDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(user);
    }
}
