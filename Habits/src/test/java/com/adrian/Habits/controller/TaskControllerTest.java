package com.adrian.Habits.controller;

import com.adrian.Habits.dto.CreateTaskRequest;
import com.adrian.Habits.model.Task;
import com.adrian.Habits.service.TaskService;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    public void getTaskById() throws Exception{
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test");

        when(taskService.getTaskById(taskId)).thenReturn(task);

        mockMvc.perform(get("/api/tasks/1/get-by-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Test"));
    }

    @Test
    public void createTask() throws Exception{
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Test");
        request.setDescription("Test");
        request.setDueDate(LocalDate.of(2025,5,5));

        Task task = new Task();
        task.setId(1L);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonRequest = objectMapper.writeValueAsString(request);

        when(taskService.createTask(any(CreateTaskRequest.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test"))
                .andExpect(jsonPath("$.description").value("Test"))
                .andExpect(jsonPath("$.dueDate").value("2025-05-05"));

        verify(taskService).createTask(any(CreateTaskRequest.class));
    }
}
