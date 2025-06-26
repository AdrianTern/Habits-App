package com.adrian.Habits.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.adrian.Habits.dto.request.ChangePasswordRequest;
import com.adrian.Habits.dto.request.RegisterUserRequest;
import com.adrian.Habits.dto.response.UserResponse;
import com.adrian.Habits.exception.PasswordNotMatchException;
import com.adrian.Habits.exception.UserNotFoundException;
import com.adrian.Habits.exception.UsernameNotUniqueException;
import com.adrian.Habits.mapper.UserMapper;
import com.adrian.Habits.model.UserEntity;
import com.adrian.Habits.repository.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;
    // Password encoder to hash password
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register user
    public UserResponse registerUser(RegisterUserRequest request){
        if(userRepository.existsByUsername(request.getUsername())) throw new UsernameNotUniqueException("The username is already taken");
        UserEntity user = userRepository.save(UserMapper.toUserEntity(request, passwordEncoder));

        return UserMapper.toUserResponse(user);
    }

    // Change user password
    public UserResponse changePassword(Long id, ChangePasswordRequest request){
        UserEntity user = userRepository.findById(id)
                                        .orElseThrow(() -> new UserNotFoundException("Username not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        return UserMapper.toUserResponse(userRepository.save(user));
    }
}
