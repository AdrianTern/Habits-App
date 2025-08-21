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
import com.adrian.Habits.utils.Constants;

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
        if(userRepository.existsByUsername(request.getUsername())) throw new UsernameNotUniqueException(Constants.EXCEPTION_USERNAME_TAKEN);
        UserEntity user = userRepository.save(UserMapper.toUserEntity(request, passwordEncoder));

        return UserMapper.toUserResponse(user);
    }

    // Change user password
    public UserResponse changePassword(Long id, ChangePasswordRequest request){
        UserEntity user = userRepository.findById(id)
                                        .orElseThrow(() -> new UserNotFoundException(Constants.EXCEPTION_USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new PasswordNotMatchException(Constants.EXCEPTION_INCORRECT_OLD_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        return UserMapper.toUserResponse(userRepository.save(user));
    }
}
