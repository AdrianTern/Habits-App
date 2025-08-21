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
import com.adrian.Habits.utils.Constants;
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

    private final String username = "admin";
    private final String password = "Admin123!";

    @Test
    public void registerUser_shouldRegisterUserSuccessfully(){
        UserResponse result = authService.registerUser(RegisterUserRequest.builder()
                                                                        .username(username)
                                                                        .password(password)
                                                                        .build());
        
        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    public void registerUser_whenUsernameIsNotUnique_shouldThrowUsernameNotUniqueException(){
        userRepository.saveAndFlush(new MockUserBuilder().build());

        UsernameNotUniqueException exception = assertThrows(UsernameNotUniqueException.class, 
                                                            () -> authService.registerUser(RegisterUserRequest.builder()
                                                                                                            .username(username)
                                                                                                            .password(password)
                                                                                                            .build()));
        assertEquals(Constants.EXCEPTION_USERNAME_TAKEN, exception.getMessage());
    }

    @Test
    public void changePassword_shouldChangePasswordSuccessfully() {
        UserEntity user = userRepository.saveAndFlush(new MockUserBuilder()
                                                        .withPassword(passwordEncoder.encode(password))
                                                        .build());

        UserResponse result = authService.changePassword(user.getId(), 
                                                        ChangePasswordRequest.builder()
                                                                            .oldPassword(password)
                                                                            .newPassword("Admin456!")
                                                                            .build());
        assertNotNull(result);
        assertNotEquals(passwordEncoder.encode(password), user.getPassword());
    }

    @Test
    public void changePassword_whenOldPasswordIsNotMatch_shouldThrowPasswordNotMatchException() {
        UserEntity user = userRepository.saveAndFlush(new MockUserBuilder()
                                                        .withPassword(passwordEncoder.encode(password))
                                                        .build());
        PasswordNotMatchException exception = assertThrows(PasswordNotMatchException.class, 
                                                           () ->  authService.changePassword(user.getId(), 
                                                                                             ChangePasswordRequest.builder()
                                                                                                                .oldPassword("Admin456!")
                                                                                                                .newPassword("Admin456!")
                                                                                                                .build()));
        assertEquals(Constants.EXCEPTION_INCORRECT_OLD_PASSWORD, exception.getMessage());
    }
}
