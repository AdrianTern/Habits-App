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
    private final String username1 = "admin";
    private final String password1 = "Admin123!";
    private final String username2 = "user1";
    private final String password2 = "User123!";

    @Test
    public void getAllTasks_shouldReturnAllTasks() {
        UserEntity user1 = MockMethods.mockUser(userRepository, username1, password1);
        MockMethods.mockAllTasks(taskRepository, today, user1);
        MockMethods.assertOnAllTasks(taskService.getAllTasks());
    }

    @Test
    public void getAllTasks_whenNoTasksExists_shouldReturnEmpty() {
        List<TaskResponse> result = taskService.getAllTasks();
        assertTrue(result.isEmpty());
    }

    @Test
    public void getAllTasks_shouldOnlyReturnUserSpecificTasks() {
        // Tasks for user1
        UserEntity user1 = MockMethods.mockUser(userRepository, username1, password1);
        MockMethods.mockAllTasks(taskRepository, today, user1);

        // Tasks for user2
        UserEntity user2 = MockMethods.mockUser(userRepository, username2, password2);
        MockMethods.mockAllTasks(taskRepository, today, user2);

        // Assertion on user1, should only return 2 tasks
        MockMethods.assertOnAllTasks(taskService.getAllTasksByUser(user1.getId()));
    }

    @Test
    public void getTodayTasks_shouldReturnTodayTasks() {
        // Tasks for user1
        UserEntity user1 = MockMethods.mockUser(userRepository, username1, password1);
        MockMethods.mockTodayTasks(taskRepository, today, user1);

        // Tasks for user2
        UserEntity user2 = MockMethods.mockUser(userRepository, username2, password2);
        MockMethods.mockTodayTasks(taskRepository, today, user2);

        MockMethods.assertOnTodayTasks(taskService.getTodayTasksByUser(user1.getId(), today), today);
    }

    @Test
    public void getUpcomingTasks_shouldReturnUpcomingTasks() {
        // Tasks for user1
        UserEntity user1 = MockMethods.mockUser(userRepository, username1, password1);
        MockMethods.mockUpcomingTasks(taskRepository, today, user1);

        // Tasks for user2
        UserEntity user2 = MockMethods.mockUser(userRepository, username2, password2);
        MockMethods.mockUpcomingTasks(taskRepository, today, user2);

        MockMethods.assertOnUpcomingTasks(taskService.getUpcomingTasksByUser(user1.getId(), today), today);
    }

    @Test
    public void getOverdueTasks_shouldReturnOverdueTasks() {
        // Tasks for user1
        UserEntity user1 = MockMethods.mockUser(userRepository, username1, password1);
        MockMethods.mockOverdueTasks(taskRepository, today, user1);

        // Tasks for user2
        UserEntity user2 = MockMethods.mockUser(userRepository, username2, password2);
        MockMethods.mockOverdueTasks(taskRepository, today, user2);

        MockMethods.assertOnOverdueTasks(taskService.getOverdueTasksByUser(user1.getId(), today), today);
    }

    @Test
    public void getRoutineTasks_shouldReturnRoutineTasks() {
        // Tasks for user1
        UserEntity user1 = MockMethods.mockUser(userRepository, username1, password1);
        MockMethods.mockRoutineTasks(taskRepository, user1);

        // Tasks for user2
        UserEntity user2 = MockMethods.mockUser(userRepository, username2, password2);
        MockMethods.mockRoutineTasks(taskRepository, user2);

        MockMethods.assertOnRoutineTasks(taskService.getRoutineTasksByUser(user1.getId()));
    }

    @Test
    public void countAllTasks_shouldReturnNumberOfAllTasks() {
        // Tasks for user1
        UserEntity user1 = MockMethods.mockUser(userRepository, username1, password1);
        MockMethods.mockAllTasks(taskRepository, today, user1);

        // Tasks for user2
        UserEntity user2 = MockMethods.mockUser(userRepository, username2, password2);
        MockMethods.mockAllTasks(taskRepository, today, user2);

        long result = taskService.getAllTasksCountByUser(user1.getId());
        assertEquals(2, result);
    }

    @Test
    public void countTodayTasks_shouldReturnNumberOfTodayTasks() {
        // Tasks for user1
        UserEntity user1 = MockMethods.mockUser(userRepository, username1, password1);
        MockMethods.mockTodayTasks(taskRepository, today, user1);

        // Tasks for user2
        UserEntity user2 = MockMethods.mockUser(userRepository, username2, password2);
        MockMethods.mockTodayTasks(taskRepository, today, user2);

        long result = taskService.getTodayTaskCountByUser(user1.getId(), today);
        assertEquals(3, result);
    }

    @Test
    public void countUpcomingTasks_shouldReturnNumberOfUpcomingTasks() {
        // Tasks for user1
        UserEntity user1 = MockMethods.mockUser(userRepository, username1, password1);
        MockMethods.mockUpcomingTasks(taskRepository, today, user1);

        // Tasks for user2
        UserEntity user2 = MockMethods.mockUser(userRepository, username2, password2);
        MockMethods.mockUpcomingTasks(taskRepository, today, user2);

        long result = taskService.getUpcomingTasksCountByUser(user1.getId(), today);
        assertEquals(2, result);
    }

    @Test
    public void countOverdueTasks_shouldReturnNumberOfOverdueTasks() {
        // Tasks for user1
        UserEntity user1 = MockMethods.mockUser(userRepository, username1, password1);
        MockMethods.mockOverdueTasks(taskRepository, today, user1);

        // Tasks for user1
        UserEntity user2 = MockMethods.mockUser(userRepository, username2, password2);
        MockMethods.mockOverdueTasks(taskRepository, today, user2);

        long result = taskService.getOverdueTasksCountByUser(user1.getId(), today);
        assertEquals(1, result);
    }

    @Test
    public void countRoutineTasks_shouldReturnNumberOfRoutineTasks() {
        // Tasks for user1
        UserEntity user1 = MockMethods.mockUser(userRepository, username1, password1);
        MockMethods.mockRoutineTasks(taskRepository, user1);

        // Tasks for user2
        UserEntity user2 = MockMethods.mockUser(userRepository, username2, password2);
        MockMethods.mockRoutineTasks(taskRepository, user2);

        long result = taskService.getRoutineTasksCountByUser(user1.getId());
        assertEquals(1, result);
    }

    @Test
    public void createTask_shouldCreateTaskSuccessfully() {
        UserEntity user1 = MockMethods.mockUser(userRepository, username1, password1);
        TaskResponse result = taskService.createTask(CreateTaskRequest.builder()
                .title(title)
                .dueDate(today)
                .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                .build(), user1.getId());
        assertNotNull(result);
        assertEquals(title, result.getTitle());
        assertEquals(today.toString(), result.getDueDate());
        assertEquals(false, result.getRoutineDetailsResponse().getIsRoutineTask());
    }

    @Test
    public void updateTask_shouldUpdateTaskSuccessfully() {
        UserEntity user1 = MockMethods.mockUser(userRepository, username1, password1);
        TaskResponse task = taskService.createTask(CreateTaskRequest.builder()
                .dueDate(today)
                .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                .build(), user1.getId());
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
        UserEntity user1 = MockMethods.mockUser(userRepository, username1, password1);
        TaskResponse task = taskService.createTask(CreateTaskRequest.builder()
                .dueDate(today)
                .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                .build(), user1.getId());
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
        UserEntity user1 = MockMethods.mockUser(userRepository, username1, password1);
        TaskResponse task = taskService.createTask(CreateTaskRequest.builder()
                .dueDate(today)
                .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                .build(),user1.getId());
        taskService.deleteTask(task.getId());

        long result = taskService.getAllTasksCountByUser(user1.getId());

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
        UserEntity user1 = MockMethods.mockUser(userRepository, username1, password1);
        taskService.createTask(CreateTaskRequest.builder()
                .dueDate(today)
                .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                .build(), user1.getId());
        taskService.createTask(CreateTaskRequest.builder()
                .dueDate(today)
                .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                .build(), user1.getId());

        taskService.deleteAllTask();

        long result = taskService.getAllTasksCountByUser(user1.getId());

        assertEquals(0, result);
    }
}
