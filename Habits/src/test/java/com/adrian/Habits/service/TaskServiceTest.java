package com.adrian.Habits.service;

import com.adrian.Habits.dto.CreateTaskRequest;
import com.adrian.Habits.dto.UpdateTaskRequest;
import com.adrian.Habits.model.Task;
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
    public void getAllTasks_shouldReturnAllTasksFromRepo(){
        Task task1  = new Task();
        task1.setTitle("One");

        Task task2 = new Task();
        task2.setTitle("Two");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> result = taskService.getAllTask();

        assertEquals(2, result.size());
        assertEquals("One", result.get(0).getTitle());
        assertEquals("Two", result.get(1).getTitle());
    }

    @Test
    public void getTaskById_found(){
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(taskId);

        assertNotNull(result);
        assertEquals(taskId, result.getId());

        verify(taskRepository).findById(taskId);
    }

    @Test
    public void getTaskById_notFound(){
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.getTaskById(taskId), "Task not found");

        verify(taskRepository).findById(taskId);
    }

    @Test
    public void getTaskByTitle_found(){
        String title = "Test";

        Task task1 = new Task();
        task1.setTitle(title);

        Task task2 = new Task();
        task2.setTitle(title);

        when(taskRepository.findByTitle(title)).thenReturn(Arrays.asList(task1, task2));

        List<Task> result = taskService.getTaskByTitle(title);

        assertEquals(2, result.size());
        assertEquals(title, result.get(0).getTitle());
        assertEquals(title, result.get(1).getTitle());

        verify(taskRepository).findByTitle(title);
    }

    @Test
    public void getTaskByDueDate_shouldReturnTaskByDueDate(){
        LocalDate dueDate = LocalDate.of(2025, 5, 5);

        Task task1 = new Task();
        task1.setDueDate(dueDate);

        Task task2 = new Task();
        task2.setDueDate(dueDate);

        when(taskRepository.findByDueDate(dueDate)).thenReturn(Arrays.asList(task1, task2));

        List<Task> result = taskService.getTaskByDueDate(dueDate);

        assertEquals(2, result.size());
        assertEquals(dueDate, result.get(0).getDueDate());
        assertEquals(dueDate, result.get(1).getDueDate());

        verify(taskRepository).findByDueDate(dueDate);
    }

    @Test
    public void createTask_shouldSaveAndReturnTask(){
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Test");
        request.setDescription("Test");
        request.setDueDate(LocalDate.now());

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(request);

        assertNotNull(result);
        assertEquals(task, result);
        verify(taskRepository).save(any(Task.class));

    }

    @Test
    public void updateTask_shouldSaveAndUpdateTask(){
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Test1");

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("Test2");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        Task result = taskService.updateTask(taskId, request);

        assertNotNull(result);
        assertEquals("Test2", result.getTitle());
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void toggleIsComplete_shouldSaveAndUpdateTask(){
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test");
        task.setIsComplete(false);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.toggleIsComplete(taskId);

        assertNotNull(result);
        assertEquals(true, result.getIsComplete());

        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void deleteTaskById_shouldDeleteTask(){
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.deleteTask(taskId);

        verify(taskRepository).findById(taskId);
        verify(taskRepository).delete(task);
    }

    @Test
    public void deleteAllTask_shouldDeleteAll(){
        taskService.deleteAllTask();

        verify(taskRepository).deleteAll();
    }
}
