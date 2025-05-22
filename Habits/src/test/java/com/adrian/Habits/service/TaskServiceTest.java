package com.adrian.Habits.service;

import com.adrian.Habits.dto.CreateTaskRequest;
import com.adrian.Habits.dto.TaskResponse;
import com.adrian.Habits.dto.UpdateTaskRequest;
import com.adrian.Habits.model.TaskEntity;
import com.adrian.Habits.repository.TaskRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void getAllTasks_shouldReturnAllTasksFromRepo() {
        TaskEntity task1 = TaskEntity.builder()
                                     .title("One")
                                     .build();

        TaskEntity task2 = TaskEntity.builder()
                                     .title("Two")
                                     .build();

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<TaskResponse> result = taskService.getAllTask();

        assertEquals(2, result.size());
        assertEquals("One", result.get(0).getTitle());
        assertEquals("Two", result.get(1).getTitle());
    }

    @Test
    public void getTaskById_found() {
        Long taskId = 1L;
        TaskEntity task = TaskEntity.builder()
                                    .id(taskId)
                                    .title("Test")
                                    .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        TaskResponse result = taskService.getTaskById(taskId);

        assertNotNull(result);
        assertEquals(taskId, result.getId());

        verify(taskRepository).findById(taskId);
    }

    @Test
    public void getTaskById_notFound() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.getTaskById(taskId), "Task not found");

        verify(taskRepository).findById(taskId);
    }

    @Test
    public void getTaskByTitle_found() {
        String title = "Test";

        TaskEntity task1 = TaskEntity.builder()
                                     .title(title)
                                     .build();

        TaskEntity task2 = TaskEntity.builder()
                                     .title(title)
                                     .build();

        when(taskRepository.findByTitle(title)).thenReturn(Arrays.asList(task1, task2));

        List<TaskResponse> result = taskService.getTaskByTitle(title);

        assertEquals(2, result.size());
        assertEquals(title, result.get(0).getTitle());
        assertEquals(title, result.get(1).getTitle());

        verify(taskRepository).findByTitle(title);
    }

    @Test
    public void getTaskByDueDate_shouldReturnTaskByDueDate() {
        LocalDate dueDate = LocalDate.of(2025, 5, 5);

        TaskEntity task1 = TaskEntity.builder()
                                     .dueDate(dueDate)
                                     .build();

        TaskEntity task2 = TaskEntity.builder()
                                     .dueDate(dueDate)
                                     .build();

        when(taskRepository.findByDueDate(dueDate)).thenReturn(Arrays.asList(task1, task2));

        List<TaskResponse> result = taskService.getTaskByDueDate(dueDate);

        assertEquals(2, result.size());
        assertEquals(dueDate, LocalDate.parse(result.get(0).getDueDate()));
        assertEquals(dueDate, LocalDate.parse(result.get(1).getDueDate()));

        verify(taskRepository).findByDueDate(dueDate);
    }

    @Test
    public void createTask_shouldSaveAndReturnTask() {
        CreateTaskRequest request = CreateTaskRequest.builder()
                                                        .title("Test")
                                                        .description("Test")
                                                        .dueDate(LocalDate.now())
                                                        .build();

        TaskEntity task = TaskEntity.builder()
                                        .title(request.getTitle())
                                        .description(request.getDescription())
                                        .dueDate(request.getDueDate())
                                        .build();

        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);

        TaskResponse result = taskService.createTask(request);

        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        assertEquals(task.getTitle(), result.getTitle());
        assertEquals(task.getDescription(), result.getDescription());
        assertEquals(task.getDueDate().toString(), result.getDueDate());
        assertEquals(task.getIsCompleted(), result.getIsCompleted());

        verify(taskRepository).save(any(TaskEntity.class));

    }

    @Test
    public void updateTask_shouldSaveAndUpdateTask() {
        Long taskId = 1L;
        TaskEntity existingTask = TaskEntity.builder()
                                                .id(taskId)
                                                .title("Test1")
                                                .build();

        UpdateTaskRequest request = UpdateTaskRequest.builder()
                                                        .title("Test2")
                                                        .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(existingTask);

        TaskResponse result = taskService.updateTask(taskId, request);

        assertNotNull(result);
        assertEquals("Test2", result.getTitle());

        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(TaskEntity.class));
    }

    @Test
    public void toggleIsComplete_shouldSaveAndUpdateTask() {
        Long taskId = 1L;
        TaskEntity task = TaskEntity.builder()
                                        .id(taskId)
                                        .title("Test")
                                        .isCompleted(false)
                                        .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);

        TaskResponse result = taskService.toggleIsComplete(taskId);

        assertNotNull(result);
        assertEquals(true, result.getIsCompleted());

        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(TaskEntity.class));
    }

    @Test
    public void deleteTaskById_shouldDeleteTask() {
        Long taskId = 1L;
        TaskEntity task = TaskEntity.builder()
                                        .id(taskId)
                                        .title("Test")
                                        .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.deleteTask(taskId);

        verify(taskRepository).findById(taskId);
        verify(taskRepository).delete(task);
    }

    @Test
    public void deleteAllTask_shouldDeleteAll() {
        taskService.deleteAllTask();

        verify(taskRepository).deleteAll();
    }
}
