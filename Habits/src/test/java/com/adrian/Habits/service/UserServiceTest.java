package com.adrian.Habits.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.adrian.Habits.dto.response.UserResponse;
import com.adrian.Habits.model.UserEntity;
import com.adrian.Habits.repository.UserRepository;
import com.adrian.Habits.utils.MockUserBuilder;

// Integration tests for UserService
@SpringBootTest
@Transactional
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void getAllUsers_shouldReturnAllUsers() {
        userRepository.save(new MockUserBuilder().build());
        userRepository.save(new MockUserBuilder().withUsername("admin1").build());
        userRepository.flush();

        List<UserResponse> result = userService.getAllUsers();
        
        assertEquals(2, result.size());
    }

   

    @Test
    public void deleteUserById_shouldDeleteUserSuccessfully() {
        UserEntity user = userRepository.saveAndFlush(new MockUserBuilder().build());
        userService.deleteById(user.getId());

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(0, result.size());
    }

    @Test
    public void deleteAllUsers_shouldDeleteAllUsersSuccessfully() {
        userRepository.save(new MockUserBuilder().build());
        userRepository.save(new MockUserBuilder().withUsername("admin1").build());
        userRepository.flush();

        userService.deleteAllUsers();

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(0, result.size());
    }
}
