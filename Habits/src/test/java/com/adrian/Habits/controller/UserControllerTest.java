package com.adrian.Habits.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
import com.adrian.Habits.dto.request.CreateUserRequest;
import com.adrian.Habits.model.UserEntity;
import com.adrian.Habits.repository.UserRepository;
import com.adrian.Habits.utils.MockMethods;
import com.adrian.Habits.utils.MockUserBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.adrian.Habits.dto.response.UserResponse;

// Integration tests for UserController
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String BASE_URL = "/api/users";

    public String getUrlWithId(Long id) {
        return BASE_URL + "/" + id;
    }

    @Test
    public void getAllUsers_shouldReturnAllUsers() throws Exception {
        userRepository.save(new MockUserBuilder().build());
        userRepository.save(new MockUserBuilder().withUsername("admin1").build());
        userRepository.flush();

        MvcResult result = mockMvc.perform(get(BASE_URL))
                                  .andExpect(status().isOk())
                                  .andReturn();

        String content = result.getResponse().getContentAsString();

        List<UserResponse> users = MockMethods.parseJson(objectMapper, content, new TypeReference<>() {});

        assertEquals(2, users.size());
    }

    @Test
    public void getUserById_shouldReturnUserBasedOnId() throws Exception {
        UserEntity user = userRepository.saveAndFlush(new MockUserBuilder().build());

        MvcResult result = mockMvc.perform(get(getUrlWithId(user.getId())))
                                  .andExpect(status().isOk())
                                  .andReturn();

        String content = result.getResponse().getContentAsString();

        UserResponse foundUser = MockMethods.parseJson(objectMapper, content, new TypeReference<>() {});
        
        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    public void getUserById_whenIdIsInvalid_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get(getUrlWithId(1L) )) .andExpect(status().isBadRequest());
    }

    @Test
    public void createUser_shouldSuccessfullyCreateUser() throws Exception{
        CreateUserRequest request = CreateUserRequest.builder()
                                                    .username("admin")
                                                    .password("admin123")
                                                    .build();

        String json = objectMapper.writeValueAsString(request);
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isCreated());

    }

    @Test
    public void createUser_whenUsernameIsNotUnique_shouldReturnBadRequest() throws Exception {
        userRepository.saveAndFlush(new MockUserBuilder().build());

        CreateUserRequest request = CreateUserRequest.builder()
                                                    .username("admin")
                                                    .password("admin123")
                                                    .build();
        
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(BASE_URL)
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
    public void deleteUserById_shouldDeleteUserSuccessfully() throws Exception{
        UserEntity user = userRepository.saveAndFlush(new MockUserBuilder().build());

        mockMvc.perform(delete(BASE_URL + "/" + user.getId())).andExpect(status().isNoContent());
    }

    @Test
    public void deleteAllUsers_shouldDeleteAllUsersSuccessfully() throws Exception{
        userRepository.save(new MockUserBuilder().build());
        userRepository.save(new MockUserBuilder().withUsername("admin1").build());
        userRepository.flush();

        mockMvc.perform(delete(BASE_URL)).andExpect(status().isNoContent());
    }
}
