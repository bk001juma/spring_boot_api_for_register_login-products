package com.example.barack.controller;

import com.example.barack.dto.UserLoginRequest;
import com.example.barack.dto.UserRegistrationRequest;
import com.example.barack.entity.User;
import com.example.barack.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest userRequest) {
        try {
            User user = new User();
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            user.setPassword(userRequest.getPassword());
            User newUser = userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully with ID: " + newUser.getId());
        } catch (RuntimeException e) {
            log.error("Error registering user: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    // Authenticate a user
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginRequest loginRequest) {
        try {
            User user = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok("User authenticated successfully with ID: " + user.getId());
        } catch (RuntimeException e) {
            log.error("Error logging in user: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }
}
