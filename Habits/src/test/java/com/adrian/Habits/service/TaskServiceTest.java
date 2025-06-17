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
import com.adrian.Habits.repository.TaskRepository;
import com.adrian.Habits.utils.MockMethods;

// Integration tests for TaskService
@SpringBootTest
@Transactional
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    private final Long taskId = 1L;
    private final String title = "mock";
    private final LocalDate today = LocalDate.of(2025, 5, 5);
    private final LocalDate tomorrow = today.plusDays(1);
    private final String exceptionMsg = "Task not found";

    @Test
    public void getAllTasks_shouldReturnAllTasks() {
        MockMethods.mockAllTasks(taskRepository, today);
        MockMethods.assertOnAllTasks(taskService.getAllTasks());
    }

    @Test
    public void getAllTasks_whenNoTasksExists_shouldReturnEmpty() {
        List<TaskResponse> result = taskService.getAllTasks();
        assertTrue(result.isEmpty());
    }

    @Test
    public void getTodayTasks_shouldReturnTodayTasks() {
         MockMethods.mockTodayTasks(taskRepository, today); 
         MockMethods.assertOnTodayTasks(taskService.getTodayTasks(today), today);
    }

    @Test
    public void getUpcomingTasks_shouldReturnUpcomingTasks() {
        MockMethods.mockUpcomingTasks(taskRepository, today);
        MockMethods.assertOnUpcomingTasks(taskService.getUpcomingTasks(today), today);
    }

    @Test
    public void getOverdueTasks_shouldReturnOverdueTasks() {
        MockMethods.mockOverdueTasks(taskRepository, today);
        MockMethods.assertOnOverdueTasks(taskService.getOverdueTasks(today), today);
    }

    @Test
    public void getRoutineTasks_shouldReturnRoutineTasks() {
        MockMethods.mockRoutineTasks(taskRepository);
        MockMethods.assertOnRoutineTasks(taskService.getRoutineTasks());
    }

    @Test
    public void countAllTasks_shouldReturnNumberOfAllTasks() {
        MockMethods.mockAllTasks(taskRepository, today);
        long result = taskService.getAllTasksCount();
        assertEquals(2, result);
    }

    @Test
    public void countTodayTasks_shouldReturnNumberOfTodayTasks() {
        MockMethods.mockTodayTasks(taskRepository, today);       
        long result = taskService.getTodayTaskCount(today);
        assertEquals(3, result);
    }

    @Test
    public void countUpcomingTasks_shouldReturnNumberOfUpcomingTasks() {
        MockMethods.mockUpcomingTasks(taskRepository, today);
        long result = taskService.getUpcomingTasksCount(today);
        assertEquals(2, result);
    }

    @Test
    public void countOverdueTasks_shouldReturnNumberOfOverdueTasks() {
        MockMethods.mockOverdueTasks(taskRepository, today);
        long result = taskService.getOverdueTasksCount(today);
        assertEquals(1, result);
    }

    @Test
    public void countRoutineTasks_shouldReturnNumberOfRoutineTasks() {
        MockMethods.mockRoutineTasks(taskRepository);
        long result = taskService.getRoutineTasksCount();
        assertEquals(1, result);
    }

    @Test
    public void createTask_shouldCreateTaskSuccessfully() {
        TaskResponse result = taskService.createTask(CreateTaskRequest.builder()
                                                                    .title(title)
                                                                    .dueDate(today)
                                                                    .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                                                                    .build());
        assertNotNull(result);
        assertEquals(title, result.getTitle());
        assertEquals(today.toString(), result.getDueDate());
        assertEquals(false, result.getRoutineDetailsResponse().getIsRoutineTask());
    }

    @Test
    public void updateTask_shouldUpdateTaskSuccessfully() {
        TaskResponse task = taskService.createTask(CreateTaskRequest.builder()
                                                                    .dueDate(today)
                                                                    .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                                                                    .build());
        TaskResponse result = taskService.updateTask(task.getId(), UpdateTaskRequest.builder().dueDate(tomorrow).build());

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
        TaskResponse task = taskService.createTask(CreateTaskRequest.builder()
                                                                    .dueDate(today)
                                                                    .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                                                                    .build());
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
        TaskResponse task = taskService.createTask(CreateTaskRequest.builder()
                                                                    .dueDate(today)
                                                                    .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                                                                    .build());
        taskService.deleteTask(task.getId());

        long result = taskService.getAllTasksCount();

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
        taskService.createTask(CreateTaskRequest.builder()
                                                .dueDate(today)
                                                .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                                                .build());
        taskService.createTask(CreateTaskRequest.builder()
                                                .dueDate(today)
                                                .routineDetailsRequest(MockMethods.mockRoutineDetailsRequest(false))
                                                .build());

        taskService.deleteAllTask();

        long result = taskService.getAllTasksCount();

        assertEquals(0, result);
    }
}
