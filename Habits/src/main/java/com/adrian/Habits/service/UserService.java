package com.adrian.Habits.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.adrian.Habits.mapper.UserMapper;
import com.adrian.Habits.model.UserEntity;
import com.adrian.Habits.repository.UserRepository;
import com.adrian.Habits.dto.request.ChangePasswordRequest;
import com.adrian.Habits.dto.request.CreateUserRequest;
import com.adrian.Habits.dto.response.UserResponse;
import com.adrian.Habits.exception.PasswordNotMatchException;
import com.adrian.Habits.exception.UserNotFoundException;
import com.adrian.Habits.exception.UsernameNotUniqueException;

// Service class for user
@Service
public class UserService {

    private final UserRepository userRepository;
    // Password encoder to hash password
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Get all users
    public List<UserResponse> getAllUsers(){
        return userRepository.findAll()
                            .stream()
                            .map(UserMapper::toUserResponse)
                            .toList();
    }

    // Get user by ID
    public UserResponse getUserById(Long id){
        UserEntity user = userRepository.findById(id)
                                        .orElseThrow(() -> new UserNotFoundException("Invalid User ID")); 

        return UserMapper.toUserResponse(user);
    }

    // Create user
    public UserResponse createUser(CreateUserRequest request){
        if(userRepository.existsByUsername(request.getUsername())) throw new UsernameNotUniqueException("The username is already taken");
        UserEntity user = userRepository.save(UserMapper.toUserEntity(request, passwordEncoder));

        return UserMapper.toUserResponse(user);
    }

    // Change user password
    public UserResponse changePassword(Long id, ChangePasswordRequest request){
        UserEntity user = userRepository.findById(id)
                                        .orElseThrow(() -> new UserNotFoundException("Invalid user ID"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        return UserMapper.toUserResponse(userRepository.save(user));
    }

    // Delete user by ID
    public void deleteById(Long id){
        userRepository.deleteById(id);  
    }

    // Delete all users
    public void deleteAllUsers(){
        userRepository.deleteAll();
    }
}
