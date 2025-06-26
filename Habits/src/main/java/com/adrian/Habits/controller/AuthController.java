package com.adrian.Habits.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adrian.Habits.dto.request.ChangePasswordRequest;
import com.adrian.Habits.dto.request.RegisterUserRequest;
import com.adrian.Habits.dto.request.LoginRequest;
import com.adrian.Habits.dto.response.LoginResponse;
import com.adrian.Habits.dto.response.UserResponse;
import com.adrian.Habits.exception.UserNotFoundException;
import com.adrian.Habits.mapper.UserMapper;
import com.adrian.Habits.model.UserEntity;
import com.adrian.Habits.repository.UserRepository;
import com.adrian.Habits.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final PasswordEncoder passwordEncoder;

    private final AuthService authService;

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository, AuthService authService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    // Register user
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse user = authService.registerUser(request);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // Change user password
    @PatchMapping("/changePassword/{id}")
    public ResponseEntity<UserResponse> changePassword(@PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(authService.changePassword(id, request));
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        UserEntity user = userRepository.findByUsername(request.getUsername())
                                        .orElseThrow(() -> new UserNotFoundException("Username not found"));

        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.ok(UserMapper.toLoginResponse(user));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
