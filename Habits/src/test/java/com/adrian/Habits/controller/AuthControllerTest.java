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
import com.adrian.Habits.utils.Constants;
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

    private final String BASE_URL = Constants.ENDPOINT_AUTH_BASE;
    private final String username = "admin";
    private final String password = "Admin123!";

    @Test
    public void registerUser_shouldSuccessfullyRegisterUser() throws Exception{
        RegisterUserRequest request = RegisterUserRequest.builder()
                                                        .username(username)
                                                        .password(password)
                                                        .build();

        String json = objectMapper.writeValueAsString(request);
        mockMvc.perform(post(BASE_URL + Constants.ENDPOINT_AUTH_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isCreated());

    }

    @Test
    public void registerUser_whenUsernameIsNotUnique_shouldReturnBadRequest() throws Exception {
        userRepository.saveAndFlush(new MockUserBuilder().build());

        RegisterUserRequest request = RegisterUserRequest.builder()
                                                    .username(username)
                                                    .password(password)
                                                    .build();
        
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(BASE_URL + Constants.ENDPOINT_AUTH_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isBadRequest());
    }

    @Test
    public void registerUser_whenPasswordIsInvalid_shouldReturnBadRequest() throws Exception {
        userRepository.saveAndFlush(new MockUserBuilder().build());

        RegisterUserRequest request = RegisterUserRequest.builder()
                                                    .username(username)
                                                    .password("admin123")
                                                    .build();
        
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(BASE_URL + Constants.ENDPOINT_AUTH_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isBadRequest());
    }

    @Test
    public void changePassword_shouldChangePasswordSuccessfully() throws Exception{
        UserEntity user = userRepository.saveAndFlush(new MockUserBuilder()
                                                        .withPassword(passwordEncoder.encode(password))
                                                        .build());

        ChangePasswordRequest request = ChangePasswordRequest.builder()
                                                            .oldPassword(password)
                                                            .newPassword("123Admin!")
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
                                                            .newPassword("123Admin!")
                                                            .build();
        
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch(BASE_URL + "/changePassword/" + user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void changePassword_whenNewPasswordIsInvalid_shouldReturnBadRequest() throws Exception{
        UserEntity user = userRepository.saveAndFlush(new MockUserBuilder()
                                                        .withPassword(passwordEncoder.encode(password))
                                                        .build());

        ChangePasswordRequest request = ChangePasswordRequest.builder()
                                                            .oldPassword(password)
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
                                                        .withPassword(passwordEncoder.encode(password))
                                                        .build());

        AuthRequest request = AuthRequest.builder()
                                        .username(user.getUsername())
                                        .password(password)
                                        .build();

        String json = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post(BASE_URL + Constants.ENDPOINT_AUTH_LOGIN)
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
                                    .withPassword(passwordEncoder.encode(password))
                                    .build());

        AuthRequest request = AuthRequest.builder()
                                        .username("user")
                                        .password(password)
                                        .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(BASE_URL + Constants.ENDPOINT_AUTH_LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginUser_whenPasswordIsNotMatch_shouldReturnUnauthorized() throws Exception {
        userRepository.saveAndFlush(new MockUserBuilder() 
                                    .withPassword(passwordEncoder.encode(password))
                                    .build());
        
        AuthRequest request = AuthRequest.builder()
                                        .username(username)
                                        .password(username)
                                        .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(BASE_URL + Constants.ENDPOINT_AUTH_LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isUnauthorized());
    }
}
