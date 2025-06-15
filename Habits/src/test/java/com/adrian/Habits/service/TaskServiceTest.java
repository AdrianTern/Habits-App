package com.adrian.Habits.service;

import com.adrian.Habits.dto.request.CreateTaskRequest;
import com.adrian.Habits.dto.request.RoutineDetailsRequest;
import com.adrian.Habits.dto.request.UpdateTaskRequest;
import com.adrian.Habits.dto.response.TaskResponse;
import com.adrian.Habits.mapper.TaskMapper;
import com.adrian.Habits.model.RoutineDetails;
import com.adrian.Habits.model.TaskEntity;
import com.adrian.Habits.repository.TaskRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Test cases for TaskService
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private String title = "mock";
    private String exceptionMsg = "Task not found";
    private LocalDate today = LocalDate.of(2025, 5, 5);
    private Long taskId = 1L;

    // Mock routine details
    public RoutineDetails mockRoutineDetails(Boolean isRoutineTask) {
        return isRoutineTask ? new RoutineDetails(isRoutineTask) : null;
    }

    // Mock routine details request
    public RoutineDetailsRequest mockRoutineDetailsRequest(Boolean isRoutineTask) {
        return isRoutineTask ? new RoutineDetailsRequest(isRoutineTask) : null;
    }

    // Mock task for test
    public TaskEntity mockTask(Boolean isRoutineTask) {
        return TaskEntity.builder()
                        .id(taskId)
                        .title(title)
                        .dueDate(today)
                        .isCompleted(false)
                        .routineDetails(mockRoutineDetails(isRoutineTask))
                        .build();
    }

    // Create list of tasks for tests
    public List<TaskEntity> mockTasks(LocalDate dueDate, Boolean isRoutineTask) {
        TaskEntity task1 = TaskEntity.builder()
                                    .title(title)
                                    .dueDate(dueDate)
                                    .routineDetails(mockRoutineDetails(isRoutineTask))
                                    .build();

        TaskEntity task2 = TaskEntity.builder()
                                    .title(title)
                                    .dueDate(dueDate)
                                    .routineDetails(mockRoutineDetails(isRoutineTask))
                                    .build();

        return Arrays.asList(task1, task2);
    }

    // Assertions on the returned result
    public void assertTasks(List<TaskResponse> result, Boolean isRoutineTask) {
        for(int i = 0; i < result.size(); i++){
            assertEquals(2, result.size());
            assertEquals(title, result.get(i).getTitle());
            assertEquals(today.toString(), result.get(i).getDueDate());
            if(isRoutineTask){
                assertEquals(isRoutineTask, result.get(i).getRoutineDetailsResponse().getIsRoutineTask());
            } else{
                assertNull(result.get(i).getRoutineDetailsResponse());
            }
        }
    }

    // Assertion on empty set
    public void assertEmptySet(List<TaskResponse> result){
        assertTrue(result.isEmpty());
    }

    @Test
    public void getAllTasks_shouldReturnAllTasks() {
        when(taskRepository.findAll()).thenReturn(mockTasks(today, false));
        assertTasks(taskService.getAllTasks(), false);
        verify(taskRepository).findAll();
    }

    @Test
    public void getAllTasks_whenNoTasksExist_shouldReturnEmpty() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());
        assertEmptySet(taskService.getAllTasks());
        verify(taskRepository).findAll();
    }

    @Test
    public void getTodayTasks_shouldReturnTodayTasks() {
        when(taskRepository.findAll(any(Specification.class))).thenReturn(mockTasks(today, false));
        assertTasks(taskService.getTodayTasks(today), false);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    public void getTodayTasks_whenNoTasksExist_shouldReturnEmpty() {
        when(taskRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
        assertEmptySet(taskService.getTodayTasks(today));
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    public void getUpcomingTasks_shouldReturnUpcomingTasks() {
        when(taskRepository.findAll(any(Specification.class))).thenReturn(mockTasks(today, false));
        assertTasks(taskService.getUpcomingTasks(today.plusDays(1)), false);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    public void getUpcomingTasks_whenNoTasksExist_shouldReturnEmpty() {
        when(taskRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
        assertEmptySet(taskService.getUpcomingTasks(today.plusDays(1)));
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    public void getOverdueTasks_shouldReturnOverdueTasks() {
        when(taskRepository.findByDueDateBeforeAndIsCompletedFalse(today)).thenReturn(mockTasks(today, false));
        assertTasks(taskService.getOverdueTasks(today), false);
        verify(taskRepository).findByDueDateBeforeAndIsCompletedFalse(today);
    }

    @Test
    public void getOverdueTasks_whenNoTasksExist_shouldReturnEmpty() {
        when(taskRepository.findByDueDateBeforeAndIsCompletedFalse(today)).thenReturn(Collections.emptyList());
        assertEmptySet(taskService.getOverdueTasks(today));
        verify(taskRepository).findByDueDateBeforeAndIsCompletedFalse(today);
    }

    @Test
    public void getRoutineTasks_shouldReturnRoutineTasks() {
        when(taskRepository.findByRoutineDetailsIsRoutineTaskTrue()).thenReturn(mockTasks(today, true));
        assertTasks(taskService.getRoutineTasks(), true);
        verify(taskRepository).findByRoutineDetailsIsRoutineTaskTrue();
    }

    @Test
    public void getRoutineTasks_whenNoTasksExist_shouldReturnEmpty() {
        when(taskRepository.findByRoutineDetailsIsRoutineTaskTrue()).thenReturn(Collections.emptyList());
        assertEmptySet(taskService.getRoutineTasks());
        verify(taskRepository).findByRoutineDetailsIsRoutineTaskTrue();
    }

    @Test
    public void getAllTaskCount_shouldReturnNumberOfAllTasks(){
        List<TaskEntity> tasks = mockTasks(today, false);

        when(taskRepository.count()).thenReturn((long)tasks.size());
        
        long result = taskService.getAllTasksCount();

        assertTrue(result == tasks.size());
        verify(taskRepository).count();
    }

    @Test
    public void getTodayTaskCount_shouldReturnNumberOfTodayTasks(){
        List<TaskEntity> tasks = mockTasks(today, false);

        when(taskRepository.count(any(Specification.class))).thenReturn((long)tasks.size());
        
        long result = taskService.getTodayTaskCount(today);

        assertTrue(result == tasks.size());
        verify(taskRepository).count(any(Specification.class));
    }

    @Test
    public void getUpcomingTaskCount_shouldReturnNumberOfUpcomingTasks(){
        List<TaskEntity> tasks = mockTasks(today, false);

        when(taskRepository.count(any(Specification.class))).thenReturn((long)tasks.size());
        
        long result = taskService.getUpcomingTasksCount(today);

        assertTrue(result == tasks.size());
        verify(taskRepository).count(any(Specification.class));
    }

    @Test
    public void getOverdueTaskCount_shouldReturnNumberOfOverdueTasks(){
        List<TaskEntity> tasks = mockTasks(today, false);

        when(taskRepository.countByDueDateBeforeAndIsCompletedFalse(today)).thenReturn((long)tasks.size());
        
        long result = taskService.getOverdueTasksCount(today);

        assertTrue(result == tasks.size());
        verify(taskRepository).countByDueDateBeforeAndIsCompletedFalse(today);
    }

    @Test
    public void getRoutineTaskCount_shouldReturnNumberOfRoutineTasks(){
        List<TaskEntity> tasks = mockTasks(today, false);

        when(taskRepository.countByRoutineDetailsIsRoutineTaskTrue()).thenReturn((long)tasks.size());
        
        long result = taskService.getRoutineTasksCount();

        assertTrue(result == tasks.size());
        verify(taskRepository).countByRoutineDetailsIsRoutineTaskTrue();
    }

    @Test
    public void createTask_shouldSaveAndReturnTask() {
        CreateTaskRequest request = CreateTaskRequest.builder()
                                                    .title(title)
                                                    .dueDate(today)
                                                    .build();

        TaskEntity task = TaskMapper.toTaskEntity(request);

        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);

        TaskResponse result = taskService.createTask(request);

        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        assertEquals(task.getTitle(), result.getTitle());
        assertEquals(task.getDescription(), result.getDescription());
        assertEquals(task.getDueDate().toString(), result.getDueDate());
        assertEquals(task.getIsCompleted(), result.getIsCompleted());
        assertEquals(task.getRoutineDetails(), result.getRoutineDetailsResponse());

        verify(taskRepository).save(any(TaskEntity.class));

    }

    @Test
    public void createRoutineTask_shouldSaveAndReturnTask() {
        CreateTaskRequest request = CreateTaskRequest.builder()
                                                    .title(title)
                                                    .dueDate(today)
                                                    .routineDetailsRequest(new RoutineDetailsRequest(true))
                                                    .build();

        TaskEntity task = TaskMapper.toTaskEntity(request);

        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);

        TaskResponse result = taskService.createTask(request);

        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        assertEquals(task.getTitle(), result.getTitle());
        assertEquals(task.getDescription(), result.getDescription());
        assertEquals(task.getDueDate().toString(), result.getDueDate());
        assertEquals(task.getIsCompleted(), result.getIsCompleted());
        assertEquals(task.getRoutineDetails().getIsRoutineTask(), result.getRoutineDetailsResponse().getIsRoutineTask());

        verify(taskRepository).save(any(TaskEntity.class));

    }

    @Test
    public void updateTask_shouldSaveAndUpdateTask() {
        UpdateTaskRequest request = UpdateTaskRequest.builder()
                                                    .dueDate(today.plusDays(1))
                                                    .routineDetailsRequest(mockRoutineDetailsRequest(true))
                                                    .build();

        TaskEntity task = mockTask(false);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);

        TaskResponse result = taskService.updateTask(taskId, request);

        assertNotNull(result);
        assertEquals(today.plusDays(1).toString(), result.getDueDate());
        assertNotNull(result.getRoutineDetailsResponse());
        assertTrue(result.getRoutineDetailsResponse().getIsRoutineTask());

        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(TaskEntity.class));
    }

    @Test
    public void updateTask_whenTaskIdNotFound_shouldThrowException() {
        UpdateTaskRequest request = UpdateTaskRequest.builder()
                                                    .dueDate(today.plusDays(1))
                                                    .routineDetailsRequest(mockRoutineDetailsRequest(true))
                                                    .build();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(null, request);
        });
        
        assertEquals(exceptionMsg, exception.getMessage());
        verify(taskRepository).findById(null);
    }

    @Test
    public void toggleTask_shouldSaveAndUpdateIsCompleted() {
        TaskEntity task = mockTask(false);
        Boolean prevIsCompleted = task.getIsCompleted();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);

        TaskResponse result = taskService.toggleIsComplete(taskId);

        assertNotNull(result);
        assertEquals(!prevIsCompleted, result.getIsCompleted());

        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(TaskEntity.class));
    }

    @Test
    public void toggleTask_whenTaskIdNotFound_shouldThrowException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            taskService.toggleIsComplete(null);
        });

        assertEquals(exceptionMsg, exception.getMessage());
        verify(taskRepository).findById(null);
    }

    @Test
    public void deleteTask_shouldDeleteTask() {
        TaskEntity task = mockTask(false);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.deleteTask(taskId);

        verify(taskRepository).findById(taskId);
        verify(taskRepository).delete(task);
    }

    @Test
    public void deleteTask_whenTaskIdNotFound_shouldThrowException() {
        RuntimeException exception = assertThrows(RuntimeException.class, ()->{
            taskService.deleteTask(null);
        });

        assertEquals(exceptionMsg, exception.getMessage());
        verify(taskRepository).findById(null);
    }

    @Test
    public void deleteAllTasks_shouldDeleteAll() {
        taskService.deleteAllTask();
        verify(taskRepository).deleteAll();
    }
}
