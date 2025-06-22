package com.adrian.Habits.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.adrian.Habits.dto.request.ChangePasswordRequest;
import com.adrian.Habits.dto.request.CreateUserRequest;
import com.adrian.Habits.dto.response.UserResponse;
import com.adrian.Habits.exception.PasswordNotMatchException;
import com.adrian.Habits.exception.UserNotFoundException;
import com.adrian.Habits.exception.UsernameNotUniqueException;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void getAllUsers_shouldReturnAllUsers() {
        userRepository.save(new MockUserBuilder().build());
        userRepository.save(new MockUserBuilder().withUsername("admin1").build());
        userRepository.flush();

        List<UserResponse> result = userService.getAllUsers();
        
        assertEquals(2, result.size());
    }

    @Test
    public void getUserById_shouldReturnUserBasedOnId() {
        UserEntity user = userRepository.saveAndFlush(new MockUserBuilder().build());

        UserResponse result = userService.getUserById(user.getId());

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }

    @Test
    public void getUserById_whenIdIsInvalid_shouldThrowUserNotFoundException() {
         UserNotFoundException exception = assertThrows(UserNotFoundException.class, 
                                                        () -> userService.getUserById(1L));

        assertEquals("Invalid User ID", exception.getMessage());
    }

    @Test
    public void createUser_shouldCreateUserSuccessfully(){
        UserResponse result = userService.createUser(CreateUserRequest.builder()
                                                                    .username("admin")
                                                                    .password("admin123")
                                                                    .build());
        
        assertNotNull(result);
        assertEquals("admin", result.getUsername());
    }

    @Test
    public void createUser_whenUsernameIsNotUnique_shouldThrowUsernameNotUniqueException(){
        userRepository.saveAndFlush(new MockUserBuilder().build());

        UsernameNotUniqueException exception = assertThrows(UsernameNotUniqueException.class, 
                                                            () -> userService.createUser(CreateUserRequest.builder()
                                                                                                        .username("admin")
                                                                                                        .password("admin123")
                                                                                                        .build()));
        assertEquals("The username is already taken", exception.getMessage());
    }

    @Test
    public void changePassword_shouldChangePasswordSuccessfully() {
        UserEntity user = userRepository.saveAndFlush(new MockUserBuilder()
                                                        .withPassword(passwordEncoder.encode("admin123"))
                                                        .build());

        UserResponse result = userService.changePassword(user.getId(), 
                                                        ChangePasswordRequest.builder()
                                                                            .oldPassword("admin123")
                                                                            .newPassword("123admin")
                                                                            .build());
        assertNotNull(result);
        assertNotEquals(passwordEncoder.encode("admin123"), user.getPassword());
    }

    @Test
    public void changePassword_whenOldPasswordIsNotMatch_shouldThrowPasswordNotMatchException() {
        UserEntity user = userRepository.saveAndFlush(new MockUserBuilder()
                                                        .withPassword(passwordEncoder.encode("admin123"))
                                                        .build());
        PasswordNotMatchException exception = assertThrows(PasswordNotMatchException.class, 
                                                           () ->  userService.changePassword(user.getId(), 
                                                                                             ChangePasswordRequest.builder()
                                                                                                                .oldPassword("123admin")
                                                                                                                .newPassword("123admin")
                                                                                                                .build()));
        assertEquals("Old password is incorrect", exception.getMessage());
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
