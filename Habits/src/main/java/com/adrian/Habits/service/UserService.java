package com.adrian.Habits.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.adrian.Habits.mapper.UserMapper;
import com.adrian.Habits.repository.UserRepository;
import com.adrian.Habits.dto.response.UserResponse;

// Service class for user
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // Get all users
    public List<UserResponse> getAllUsers(){
        return userRepository.findAll()
                            .stream()
                            .map(UserMapper::toUserResponse)
                            .toList();
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
