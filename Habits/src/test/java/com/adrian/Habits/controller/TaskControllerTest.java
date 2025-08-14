package com.adrian.Habits.controller;

import com.adrian.Habits.configuration.FixedClockConfig;
import com.adrian.Habits.dto.request.CreateTaskRequest;
import com.adrian.Habits.dto.request.UpdateTaskRequest;
import com.adrian.Habits.dto.response.TaskResponse;
import com.adrian.Habits.jwt.util.JwtUtil;
import com.adrian.Habits.model.TaskEntity;
import com.adrian.Habits.model.UserEntity;
import com.adrian.Habits.repository.TaskRepository;
import com.adrian.Habits.repository.UserRepository;
import com.adrian.Habits.utils.MockMethods;
import com.adrian.Habits.utils.MockTaskBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Integration tests for TaskController
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(FixedClockConfig.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Clock clock;

    private final String BASE_URL = "/api/tasks";
    private final String TIMEZONE = "Asia/Kuala_Lumpur";
    private final String title = "mock";
    private final String username = "admin";
    private final String password = "admin123";

    public LocalDate getToday() {
        return LocalDate.now(clock);
    }

    public String getFetchTaskURL(String filter) {
        return BASE_URL + "?filter=" + filter + "&timeZone=" + TIMEZONE;
    }

    public String getTaskCountURL() {
        return BASE_URL + "/taskCount" + "?timeZone=" + TIMEZONE;
    }

    @Test
    public void getTasks_shouldReturnAllTask() throws Exception {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        MockMethods.mockAllTasks(taskRepository, getToday(), user);
        String jwt = MockMethods.getJWT(user, jwtUtil);

        MvcResult result = mockMvc.perform(get(getFetchTaskURL("all")).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        List<TaskResponse> tasks = MockMethods.parseJson(objectMapper, content, new TypeReference<>() {});

        MockMethods.assertOnAllTasks(tasks);
    }

    @Test
    public void getTasks_shouldReturnTodayTask() throws Exception {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        MockMethods.mockTodayTasks(taskRepository, getToday(), user);
        String jwt = MockMethods.getJWT(user, jwtUtil);

        MvcResult result = mockMvc.perform(get(getFetchTaskURL("today")).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        List<TaskResponse> tasks = MockMethods.parseJson(objectMapper, content, new TypeReference<>() {});

        MockMethods.assertOnTodayTasks(tasks, getToday());
    }

    @Test
    public void getTasks_shouldReturnUpcomingTask() throws Exception {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        MockMethods.mockUpcomingTasks(taskRepository, getToday(), user);
        String jwt = MockMethods.getJWT(user, jwtUtil);

        MvcResult result = mockMvc.perform(get(getFetchTaskURL("upcoming")).header("Authorization", "Bearer " + jwt))
                                .andExpect(status().isOk())
                                .andReturn();

        String content = result.getResponse().getContentAsString();

        List<TaskResponse> tasks = MockMethods.parseJson(objectMapper, content, new TypeReference<>() {});

        MockMethods.assertOnUpcomingTasks(tasks, getToday());
    }

    @Test
    public void getTasks_shouldReturnOverdueTask() throws Exception {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        MockMethods.mockOverdueTasks(taskRepository, getToday(), user);
        String jwt = MockMethods.getJWT(user, jwtUtil);

        MvcResult result = mockMvc.perform(get(getFetchTaskURL("overdue")).header("Authorization", "Bearer " + jwt))
                                .andExpect(status().isOk())
                                .andReturn();

        String content = result.getResponse().getContentAsString();

        List<TaskResponse> tasks = MockMethods.parseJson(objectMapper, content, new TypeReference<>() {});

        MockMethods.assertOnOverdueTasks(tasks, getToday());
    }

    @Test
    public void getTasks_shouldReturnRoutineTask() throws Exception {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        MockMethods.mockRoutineTasks(taskRepository, user);
        String jwt = MockMethods.getJWT(user, jwtUtil);

        MvcResult result = mockMvc.perform(get(getFetchTaskURL("routine")).header("Authorization", "Bearer " + jwt))
                                .andExpect(status().isOk())
                                .andReturn();

        String content = result.getResponse().getContentAsString();

        List<TaskResponse> tasks = MockMethods.parseJson(objectMapper, content, new TypeReference<>() {});

        MockMethods.assertOnRoutineTasks(tasks);
    }

    @Test
    public void getTasks_whenFilterIsInvalid_shouldReturnAllTasks() throws Exception {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        MockMethods.mockAllTasks(taskRepository, getToday(), user);
        String jwt = MockMethods.getJWT(user, jwtUtil);

        // Set filter as blank
        MvcResult result = mockMvc.perform(get(getFetchTaskURL("")).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        List<TaskResponse> tasks = MockMethods.parseJson(objectMapper, content, new TypeReference<>() {});

        MockMethods.assertOnAllTasks(tasks);
    }

    @Test
    public void getTaskCount_shouldReturnCountForEachTaskFilter() throws Exception {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        // Create task with due date = today (today)
        taskRepository.save(new MockTaskBuilder().withDueDate(getToday()).withUser(user).build());
        // Create task with future due date (upcoming)
        taskRepository.save(new MockTaskBuilder().withDueDate(getToday().plusDays(1)).withUser(user).build());
        // Create incomplete task with previous due date (overdue)
        taskRepository.save(new MockTaskBuilder().withDueDate(getToday().minusDays(1)).withUser(user).build());
        // Create routine task with no due date (routine) (today)
        taskRepository.save(new MockTaskBuilder().withDueDate(null).withIsRoutineTask(true).withUser(user).build());
        taskRepository.flush();

        String jwt = MockMethods.getJWT(user, jwtUtil);

        mockMvc.perform(get(getTaskCountURL()).header("Authorization", "Bearer " + jwt))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.allCount").value(4))
                        .andExpect(jsonPath("$.todayCount").value(2))
                        .andExpect(jsonPath("$.upcomingCount").value(1))
                        .andExpect(jsonPath("$.overdueCount").value(1))
                        .andExpect(jsonPath("$.routineCount").value(1));
    }

    @Test
    public void createTask_shouldCreateTaskSuccessfully() throws Exception {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        // Create request to create task
        CreateTaskRequest request = CreateTaskRequest.builder()
                                    .title(title)
                                    .dueDate(getToday())
                                    .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                                    .build();

        String json = objectMapper.writeValueAsString(request);
        String jwt = MockMethods.getJWT(user, jwtUtil);

        // Check task is created with the right title and due date
        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value(title))
            .andExpect(jsonPath("$.dueDate").value(getToday().toString()));
    }

    @Test
    public void updateTask_shouldUpdateTaskSuccessfully() throws Exception {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        // Create task with due date = today
        TaskEntity task = new MockTaskBuilder().withDueDate(getToday()).withUser(user).build();
        taskRepository.saveAndFlush(task);

        // Create request to update due date to tomorrow and set task as a routine task
        UpdateTaskRequest request = UpdateTaskRequest.builder()
                                    .title(title)
                                    .dueDate(getToday().plusDays(1))
                                    .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(true))
                                    .build();

        String json = objectMapper.writeValueAsString(request);
        String jwt = MockMethods.getJWT(user, jwtUtil);

        // Check title remains the same
        // And due date = tomorrow
        // And isRoutineTask = true
        mockMvc.perform(put(BASE_URL + "/" + task.getId())
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(title))
            .andExpect(jsonPath("$.dueDate").value(getToday().plusDays(1).toString()))
            .andExpect(jsonPath("$.routineDetailsResponse.isRoutineTask").value(true));
    }

    @Test
    public void updateTask_whenTaskIsNull_shouldReturnBadRequest() throws Exception {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        // Create request to update due date to tomorrow and set task as a routine task
        UpdateTaskRequest request = UpdateTaskRequest.builder()
                                    .title(title)
                                    .dueDate(getToday().plusDays(1))
                                    .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(true))
                                    .build();

        String json = objectMapper.writeValueAsString(request);
        String jwt = MockMethods.getJWT(user, jwtUtil);

        // Verify bad request is returned due to no task exists
        mockMvc.perform(put(BASE_URL + "/" + 1L)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void toggleTask_shouldToggleTaskSuccessfully() throws Exception {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        // Create incomplete task
        TaskEntity task = new MockTaskBuilder().withDueDate(getToday()).withIsCompleted(false).withUser(user).build();
        taskRepository.save(task);
        taskRepository.flush();

        String jwt = MockMethods.getJWT(user, jwtUtil);

        // Check isCompleted is set to true
        mockMvc.perform(patch(BASE_URL + "/" + task.getId()).header("Authorization", "Bearer " + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isCompleted").value(true));
    }

    @Test
    public void toggleTask_whenTaskIsNull_shouldReturnBadRequest() throws Exception {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        String jwt = MockMethods.getJWT(user, jwtUtil);
        // Verify bad request is returned due to no task exists
        mockMvc.perform(patch(BASE_URL + "/" + 1L).header("Authorization", "Bearer " + jwt))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteTask_shouldDeleteTaskSuccessfully() throws Exception {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        // Create task
        TaskEntity task = new MockTaskBuilder().withDueDate(getToday()).withUser(user).build();
        taskRepository.saveAndFlush(task);

        String jwt = MockMethods.getJWT(user, jwtUtil);

        // Check task is deleted
        mockMvc.perform(delete(BASE_URL + "/" + task.getId()).header("Authorization", "Bearer " + jwt))
            .andExpect(status().isNoContent());
    }

    @Test
    public void deleteTask_whenTaskIsNull_shouldReturnBadRequest() throws Exception {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        String jwt = MockMethods.getJWT(user, jwtUtil);
        // Verify bad request is returned due to no task exists
        mockMvc.perform(delete(BASE_URL + "/" + 1L).header("Authorization", "Bearer " + jwt))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteAllTasks_shouldDeleteAllTasksSuccessfully() throws Exception {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        // Create tasks
        taskRepository.save(new MockTaskBuilder().withDueDate(getToday()).withUser(user).build());
        taskRepository.save(new MockTaskBuilder().withDueDate(getToday()).withUser(user).build());
        taskRepository.flush();

        String jwt = MockMethods.getJWT(user, jwtUtil);

        // Check tasks are deleted
        mockMvc.perform(delete(BASE_URL).header("Authorization", "Bearer " + jwt))
            .andExpect(status().isNoContent());
    }
}
