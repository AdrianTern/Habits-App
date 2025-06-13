package com.adrian.Habits.controller;

import com.adrian.Habits.dto.request.CreateTaskRequest;
import com.adrian.Habits.dto.request.UpdateTaskRequest;
import com.adrian.Habits.dto.response.TaskResponse;
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
    public void createTask() throws Exception{
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("Test")
                .description("Test")
                .dueDate(LocalDate.of(2025,5,5))
                .build();

        TaskResponse task = TaskResponse.builder()
                                        .id(1L)
                                        .title(request.getTitle())
                                        .description(request.getDescription())
                                        .dueDate(request.getDueDate().toString())
                                        .build();

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

    @Test
    public void updateTask() throws Exception{
        TaskResponse task = TaskResponse.builder()
                                        .id(1L)
                                        .title("Test1")
                                        .description("Test1")
                                        .dueDate("2025-05-05")
                                        .build();

        UpdateTaskRequest request = UpdateTaskRequest.builder()
                                                        .title("Test2")
                                                        .description("Test2")
                                                        .dueDate(LocalDate.of(2025,5,6))
                                                        .isCompleted(true)
                                                        .build();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate().toString());
        task.setIsCompleted(request.getIsCompleted());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonRequest = objectMapper.writeValueAsString(request);

        when(taskService.updateTask(eq(1L), any(UpdateTaskRequest.class))).thenReturn(task);

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test2"))
                .andExpect(jsonPath("$.description").value("Test2"))
                .andExpect(jsonPath("$.dueDate").value("2025-05-06"));

        verify(taskService).updateTask(eq(1L), any(UpdateTaskRequest.class));
    }

    @Test
    public void toggleTask() throws Exception{
        TaskResponse task = TaskResponse.builder()
                                        .id(1L)
                                        .title("Test")
                                        .isCompleted(false)
                                        .build();

        task.setIsCompleted(!task.getIsCompleted());

        when(taskService.toggleIsComplete(1L)).thenReturn(task);

        mockMvc.perform(patch("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.isCompleted").value(true));

        verify(taskService).toggleIsComplete(1L);
    }

    @Test
    public void deleteTask() throws Exception{
        Long taskId = 1L;

        doNothing().when(taskService).deleteTask(taskId);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(taskId);
    }
}
