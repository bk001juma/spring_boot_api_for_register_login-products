package com.example.barack.service;

import com.example.barack.entity.User;
import com.example.barack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // Save a new user
    public User saveUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        log.info("User with email: {} successfully saved", user.getEmail());
        return savedUser;
    }

    // Update an existing user
    public User updateUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            user.setCreatedAt(existingUser.get().getCreatedAt()); // Preserve original creation date
            user.setUpdatedAt(LocalDateTime.now());
            User updatedUser = userRepository.save(user);
            log.info("User with email: {} updated successfully", user.getEmail());
            return updatedUser;
        } else {
            throw new RuntimeException("User not found with email: " + user.getEmail());
        }
    }

    // Delete a user by ID
    public void deleteUser(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("User with ID: {} deleted successfully", id);
        } else {
            throw new RuntimeException("User not found with ID: " + id);
        }
    }

    // Register a new user
    public User registerUser(User user) {
        // Check if user already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with email: " + user.getEmail());
        }
        return saveUser(user);
    }

    // Authenticate a user
    public User authenticateUser(String email, String password) {
        User user = getUserByEmail(email);
        if (user.getPassword().equals(password)) {
            log.info("User with email: {} authenticated successfully", email);
            return user;
        } else {
            throw new RuntimeException("Invalid credentials for email: " + email);
        }
    }
}
