package com.adrian.Habits.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.adrian.Habits.dto.request.ChangePasswordRequest;
import com.adrian.Habits.dto.request.RegisterUserRequest;
import com.adrian.Habits.dto.response.UserResponse;
import com.adrian.Habits.exception.PasswordNotMatchException;
import com.adrian.Habits.exception.UsernameNotUniqueException;
import com.adrian.Habits.model.UserEntity;
import com.adrian.Habits.repository.UserRepository;
import com.adrian.Habits.utils.MockUserBuilder;

@SpringBootTest
@Transactional
public class AuthServiceTest {
     @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void registerUser_shouldRegisterUserSuccessfully(){
        UserResponse result = authService.registerUser(RegisterUserRequest.builder()
                                                                        .username("admin")
                                                                        .password("admin123")
                                                                        .build());
        
        assertNotNull(result);
        assertEquals("admin", result.getUsername());
    }

    @Test
    public void registerUser_whenUsernameIsNotUnique_shouldThrowUsernameNotUniqueException(){
        userRepository.saveAndFlush(new MockUserBuilder().build());

        UsernameNotUniqueException exception = assertThrows(UsernameNotUniqueException.class, 
                                                            () -> authService.registerUser(RegisterUserRequest.builder()
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

        UserResponse result = authService.changePassword(user.getId(), 
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
                                                           () ->  authService.changePassword(user.getId(), 
                                                                                             ChangePasswordRequest.builder()
                                                                                                                .oldPassword("123admin")
                                                                                                                .newPassword("123admin")
                                                                                                                .build()));
        assertEquals("Old password is incorrect", exception.getMessage());
    }
}
