package com.adrian.Habits.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.adrian.Habits.dto.request.ChangePasswordRequest;
import com.adrian.Habits.dto.request.AuthRequest;
import com.adrian.Habits.dto.request.RegisterUserRequest;
import com.adrian.Habits.dto.response.AuthResponse;
import com.adrian.Habits.jwt.util.JwtUtil;
import com.adrian.Habits.model.UserEntity;
import com.adrian.Habits.repository.UserRepository;
import com.adrian.Habits.utils.MockUserBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class AuthControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private final String BASE_URL = "/api/auth";

    @Test
    public void registerUser_shouldSuccessfullyRegisterUser() throws Exception{
        RegisterUserRequest request = RegisterUserRequest.builder()
                                                        .username("admin")
                                                        .password("admin123")
                                                        .build();

        String json = objectMapper.writeValueAsString(request);
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isCreated());

    }

    @Test
    public void registerUser_whenUsernameIsNotUnique_shouldReturnBadRequest() throws Exception {
        userRepository.saveAndFlush(new MockUserBuilder().build());

        RegisterUserRequest request = RegisterUserRequest.builder()
                                                    .username("admin")
                                                    .password("admin123")
                                                    .build();
        
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isBadRequest());
    }

    @Test
    public void changePassword_shouldChangePasswordSuccessfully() throws Exception{
        UserEntity user = userRepository.saveAndFlush(new MockUserBuilder()
                                                        .withPassword(passwordEncoder.encode("admin123"))
                                                        .build());

        ChangePasswordRequest request = ChangePasswordRequest.builder()
                                                            .oldPassword("admin123")
                                                            .newPassword("123admin")
                                                            .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch(BASE_URL + "/changePassword/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));

    }

    @Test
    public void changePassword_whenOldPasswordIsNotMatch_shouldReturnBadRequest() throws Exception {
        UserEntity user = userRepository.saveAndFlush(new MockUserBuilder().build());

        ChangePasswordRequest request = ChangePasswordRequest.builder()
                                                            .oldPassword("123admin")
                                                            .newPassword("123admin")
                                                            .build();
        
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch(BASE_URL + "/changePassword/" + user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void loginUser_shouldReturnLoginResponse() throws Exception {
        UserEntity user = userRepository.saveAndFlush(new MockUserBuilder()
                                                        .withPassword(passwordEncoder.encode("admin123"))
                                                        .build());

        AuthRequest request = AuthRequest.builder()
                                        .username(user.getUsername())
                                        .password("admin123")
                                        .build();

        String json = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post(BASE_URL + "/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").isString())
                                .andReturn();

        // Decode jwt to verify username
        String responseContent = result.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(responseContent, AuthResponse.class);

        assertEquals(user.getUsername(), jwtUtil.extractUsername(authResponse.getToken()));
    }

    @Test
    public void loginUser_whenUsernameIsInvalid_shouldReturnBadRequest() throws Exception {
        userRepository.saveAndFlush(new MockUserBuilder() 
                                    .withPassword(passwordEncoder.encode("admin123"))
                                    .build());

        AuthRequest request = AuthRequest.builder()
                                        .username("user")
                                        .password("admin123")
                                        .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(BASE_URL + "/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginUser_whenPasswordIsNotMatch_shouldReturnUnauthorized() throws Exception {
        userRepository.saveAndFlush(new MockUserBuilder() 
                                    .withPassword(passwordEncoder.encode("admin123"))
                                    .build());
        
        AuthRequest request = AuthRequest.builder()
                                        .username("admin")
                                        .password("admin")
                                        .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(BASE_URL + "/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isUnauthorized());
    }
}
