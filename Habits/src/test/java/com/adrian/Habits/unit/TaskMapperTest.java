package com.adrian.Habits.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.adrian.Habits.dto.request.CreateTaskRequest;
import com.adrian.Habits.dto.request.RoutineDetailsRequest;
import com.adrian.Habits.dto.request.UpdateTaskRequest;
import com.adrian.Habits.dto.response.RoutineDetailsResponse;
import com.adrian.Habits.dto.response.TaskResponse;
import com.adrian.Habits.mapper.TaskMapper;
import com.adrian.Habits.model.RoutineDetails;
import com.adrian.Habits.model.TaskEntity;
import com.adrian.Habits.model.UserEntity;

// Unit tests for TaskMapper
@SpringBootTest
public class TaskMapperTest {
    private final String title = "mock";
    private final String desc = "desc";
    private final LocalDate dueDate = LocalDate.of(2025, 5, 5);
    private final String username = "admin";
    private final String password = "Admin123!";

    @Test
    public void toRoutineDetails_shouldCreateRoutineDetailsFromRoutineDetailsRequest(){
        RoutineDetailsRequest request = new RoutineDetailsRequest(true);
        RoutineDetails result = TaskMapper.toRoutineDetails(request);
        
        assertNotNull(result);
        assertEquals(request.getIsRoutineTask(), result.getIsRoutineTask());
    }

    @Test
    public void toRoutineDetailsResponse_shouldCreateRoutineDetailsResponseFromRoutineDetails(){
        RoutineDetails details = new RoutineDetails(true);
        RoutineDetailsResponse result = TaskMapper.toRoutineDetailsResponse(details);
        
        assertNotNull(result);
        assertEquals(details.getIsRoutineTask(), result.getIsRoutineTask());
    }

    @Test
    public void toTaskResponse_shouldReturnTaskResponseFromTaskEntity(){
        TaskEntity task = TaskEntity.builder()
                                    .id(1L)
                                    .title(title)
                                    .description(desc)
                                    .isCompleted(true)
                                    .routineDetails(new RoutineDetails(true))
                                    .build();
        TaskResponse result = TaskMapper.toTaskResponse(task);

        assertNotNull(result);
        assertEquals(task.getId(), result.getId());
        assertEquals(task.getTitle(), result.getTitle());
        assertEquals(task.getDescription(), result.getDescription());
        assertEquals(task.getDueDate(), result.getDueDate());
        assertEquals(task.getIsCompleted(), result.getIsCompleted());
        assertEquals(task.getRoutineDetails().getIsRoutineTask(), result.getRoutineDetailsResponse().getIsRoutineTask());
    }

    @Test
    public void toTaskEntity_shouldReturnTaskEntityFromCreateRequest(){
        UserEntity user = UserEntity.builder().username(username).password(password).build();
        CreateTaskRequest request = CreateTaskRequest.builder()
                                                    .title(title)
                                                    .description(desc)
                                                    .dueDate(dueDate)
                                                    .routineDetailsRequest(new RoutineDetailsRequest(true))
                                                    .build();
        TaskEntity result = TaskMapper.toTaskEntity(request, user);

        assertNotNull(result);
        assertEquals(request.getTitle(), result.getTitle());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getDueDate(), result.getDueDate());
        assertEquals(request.getRoutineDetailsRequest().getIsRoutineTask(), result.getRoutineDetails().getIsRoutineTask());
    }

    @Test
    public void updateTaskEntity_shouldUpdateTaskWithUpdateRequest(){
        TaskEntity task = TaskEntity.builder()
                                    .id(1L)
                                    .title(title)
                                    .description(desc)
                                    .isCompleted(false)
                                    .routineDetails(new RoutineDetails(true))
                                    .build();

        UpdateTaskRequest request = UpdateTaskRequest.builder()
                                                    .title(title)
                                                    .description(desc)
                                                    .dueDate(dueDate.plusDays(1))
                                                    .isCompleted(true)
                                                    .routineDetailsRequest(new RoutineDetailsRequest(false))
                                                    .build();
        TaskMapper.updateTaskEntity(task, request);

        assertNotNull(task);
        assertEquals(task.getTitle(), request.getTitle());
        assertEquals(task.getDescription(), request.getDescription());
        assertEquals(task.getDueDate(), request.getDueDate());
        assertEquals(task.getIsCompleted(), request.getIsCompleted());
        assertEquals(task.getRoutineDetails().getIsRoutineTask(), request.getRoutineDetailsRequest().getIsRoutineTask());
    }
}
