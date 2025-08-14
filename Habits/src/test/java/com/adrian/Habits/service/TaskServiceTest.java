package com.adrian.Habits.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.adrian.Habits.dto.request.CreateTaskRequest;
import com.adrian.Habits.dto.request.UpdateTaskRequest;
import com.adrian.Habits.dto.response.TaskResponse;
import com.adrian.Habits.exception.TaskNotFoundException;
import com.adrian.Habits.model.UserEntity;
import com.adrian.Habits.repository.TaskRepository;
import com.adrian.Habits.repository.UserRepository;
import com.adrian.Habits.utils.MockMethods;

// Integration tests for TaskService
@SpringBootTest
@Transactional
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private final Long taskId = 1L;
    private final String title = "mock";
    private final LocalDate today = LocalDate.of(2025, 5, 5);
    private final LocalDate tomorrow = today.plusDays(1);
    private final String exceptionMsg = "Task not found";
    private final String username = "admin";
    private final String password = "admin123";

    @Test
    public void getAllTasks_shouldReturnAllTasks() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        MockMethods.mockAllTasks(taskRepository, today, user);
        MockMethods.assertOnAllTasks(taskService.getAllTasks());
    }

    @Test
    public void getAllTasks_whenNoTasksExists_shouldReturnEmpty() {
        List<TaskResponse> result = taskService.getAllTasks();
        assertTrue(result.isEmpty());
    }

    @Test
    public void getTodayTasks_shouldReturnTodayTasks() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        MockMethods.mockTodayTasks(taskRepository, today, user);
        MockMethods.assertOnTodayTasks(taskService.getTodayTasksByUser(user.getId(), today), today);
    }

    @Test
    public void getUpcomingTasks_shouldReturnUpcomingTasks() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        MockMethods.mockUpcomingTasks(taskRepository, today, user);
        MockMethods.assertOnUpcomingTasks(taskService.getUpcomingTasksByUser(user.getId(), today), today);
    }

    @Test
    public void getOverdueTasks_shouldReturnOverdueTasks() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        MockMethods.mockOverdueTasks(taskRepository, today, user);
        MockMethods.assertOnOverdueTasks(taskService.getOverdueTasksByUser(user.getId(), today), today);
    }

    @Test
    public void getRoutineTasks_shouldReturnRoutineTasks() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        MockMethods.mockRoutineTasks(taskRepository, user);
        MockMethods.assertOnRoutineTasks(taskService.getRoutineTasksByUser(user.getId()));
    }

    @Test
    public void countAllTasks_shouldReturnNumberOfAllTasks() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        MockMethods.mockAllTasks(taskRepository, today, user);
        long result = taskService.getAllTasksCountByUser(user.getId());
        assertEquals(2, result);
    }

    @Test
    public void countTodayTasks_shouldReturnNumberOfTodayTasks() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        MockMethods.mockTodayTasks(taskRepository, today, user);
        long result = taskService.getTodayTaskCountByUser(user.getId(), today);
        assertEquals(3, result);
    }

    @Test
    public void countUpcomingTasks_shouldReturnNumberOfUpcomingTasks() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        MockMethods.mockUpcomingTasks(taskRepository, today, user);
        long result = taskService.getUpcomingTasksCountByUser(user.getId(), today);
        assertEquals(2, result);
    }

    @Test
    public void countOverdueTasks_shouldReturnNumberOfOverdueTasks() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        MockMethods.mockOverdueTasks(taskRepository, today, user);
        long result = taskService.getOverdueTasksCountByUser(user.getId(), today);
        assertEquals(1, result);
    }

    @Test
    public void countRoutineTasks_shouldReturnNumberOfRoutineTasks() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        MockMethods.mockRoutineTasks(taskRepository, user);
        long result = taskService.getRoutineTasksCountByUser(user.getId());
        assertEquals(1, result);
    }

    @Test
    public void createTask_shouldCreateTaskSuccessfully() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        TaskResponse result = taskService.createTask(CreateTaskRequest.builder()
                .title(title)
                .dueDate(today)
                .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                .build(), user.getId());
        assertNotNull(result);
        assertEquals(title, result.getTitle());
        assertEquals(today.toString(), result.getDueDate());
        assertEquals(false, result.getRoutineDetailsResponse().getIsRoutineTask());
    }

    @Test
    public void updateTask_shouldUpdateTaskSuccessfully() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        TaskResponse task = taskService.createTask(CreateTaskRequest.builder()
                .dueDate(today)
                .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                .build(), user.getId());
        TaskResponse result = taskService.updateTask(task.getId(),
                UpdateTaskRequest.builder().dueDate(tomorrow).build());

        assertNotNull(result);
        assertEquals(tomorrow.toString(), result.getDueDate());
    }

    @Test
    public void updateTask_whenTaskIdIsInvalid_shouldThrowException() {
        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.updateTask(taskId, UpdateTaskRequest.builder().dueDate(tomorrow).build());
        });

        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    public void toggleTask_shouldToggleIsCompleted() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        TaskResponse task = taskService.createTask(CreateTaskRequest.builder()
                .dueDate(today)
                .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                .build(), user.getId());
        TaskResponse result = taskService.toggleIsComplete(task.getId());

        assertNotNull(result);
        assertEquals(!task.getIsCompleted(), result.getIsCompleted());
    }

    @Test
    public void toggleTask_whenTaskIdIsInvalid_shouldThrowException() {
        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.toggleIsComplete(taskId);
        });

        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    public void deleteTask_shouldDeleteTaskSuccessfully() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        TaskResponse task = taskService.createTask(CreateTaskRequest.builder()
                .dueDate(today)
                .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                .build(),user.getId());
        taskService.deleteTask(task.getId());

        long result = taskService.getAllTasksCountByUser(user.getId());

        assertEquals(0, result);
    }

    @Test
    public void deleteTask_whenTaskIdIsInvalid_shouldThrowException() {
        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTask(taskId);
        });

        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    public void deleteAllTask_shouldDeleteAllTaskSuccessfully() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        taskService.createTask(CreateTaskRequest.builder()
                .dueDate(today)
                .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                .build(), user.getId());
        taskService.createTask(CreateTaskRequest.builder()
                .dueDate(today)
                .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                .build(), user.getId());

        taskService.deleteAllTask();

        long result = taskService.getAllTasksCountByUser(user.getId());

        assertEquals(0, result);
    }
}
