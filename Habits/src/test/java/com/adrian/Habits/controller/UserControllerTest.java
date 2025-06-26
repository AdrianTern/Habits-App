package com.adrian.Habits.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

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
