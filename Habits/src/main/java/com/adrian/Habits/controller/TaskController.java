package com.adrian.Habits.controller;

import com.adrian.Habits.dto.request.CreateTaskRequest;
import com.adrian.Habits.dto.request.UpdateTaskRequest;
import com.adrian.Habits.dto.response.TaskResponse;
import com.adrian.Habits.jwt.details.CustomUserDetails;
import com.adrian.Habits.model.TaskCount;
import com.adrian.Habits.service.TaskService;
import com.adrian.Habits.utils.Constants;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

// Controller class that handles the endpoints for CRUD actions
@RestController
@RequestMapping(Constants.ENDPOINT_TASK_BASE)
public class TaskController {

    private final TaskService taskService;
    private final Clock clock;

    public TaskController(TaskService taskService, Clock clock) {
        this.taskService = taskService;
        this.clock = clock;
    }

    // Endpoint to get tasks according to selected filter from client
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(@AuthenticationPrincipal CustomUserDetails user, @RequestParam String filter,
            @RequestParam(required = false, defaultValue = "Etc/UTC") String timeZone) {

        Long userId = user.getId();
        
        if (filter != null) {
            ZoneId zoneId = ZoneId.of(timeZone);
            LocalDate today = Instant.now(clock).atZone(zoneId).toLocalDate();

            if ("today".equalsIgnoreCase(filter))
                return ResponseEntity.ok(taskService.getTodayTasksByUser(userId, today));
            else if ("upcoming".equalsIgnoreCase(filter))
                return ResponseEntity.ok(taskService.getUpcomingTasksByUser(userId, today));
            else if ("overdue".equalsIgnoreCase(filter))
                return ResponseEntity.ok(taskService.getOverdueTasksByUser(userId, today));
            else if ("all".equalsIgnoreCase(filter))
                return ResponseEntity.ok(taskService.getAllTasksByUser(userId));
            else if ("routine".equalsIgnoreCase(filter))
                return ResponseEntity.ok(taskService.getRoutineTasksByUser(userId));
        }

        return ResponseEntity.ok(taskService.getAllTasksByUser(userId));
    }

    // Endpoint to get number of tasks for each filter
    @GetMapping(Constants.ENDPOINT_TASK_TASKCOUNT)
    public ResponseEntity<TaskCount> getTaskCount(@AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false, defaultValue = "Etc/UTC") String timeZone) {
        ZoneId zoneId = ZoneId.of(timeZone);
        LocalDate today = Instant.now(clock).atZone(zoneId).toLocalDate();
        Long userId = user.getId();

        TaskCount taskCount = TaskCount.builder()
                .todayCount(taskService.getTodayTaskCountByUser(userId, today))
                .upcomingCount(taskService.getUpcomingTasksCountByUser(userId, today))
                .overdueCount(taskService.getOverdueTasksCountByUser(userId, today))
                .allCount(taskService.getAllTasksCountByUser(userId))
                .routineCount(taskService.getRoutineTasksCountByUser(userId))
                .build();
        return ResponseEntity.ok(taskCount);
    }

    // Endpoint to create a new task
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request, @AuthenticationPrincipal CustomUserDetails user) {
        TaskResponse task = taskService.createTask(request, user.getId());

        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    // Endpoint to update a task
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest request, @AuthenticationPrincipal CustomUserDetails user) {
        TaskResponse task = taskService.updateTask(id, request);

        return ResponseEntity.ok(task);
    }

    // Endpoint to toggle completion of a task
    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> toggleTask(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        TaskResponse task = taskService.toggleIsComplete(id);

        return ResponseEntity.ok(task);
    }

    // Endpoint to delete a task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }

    // Endpoint to delete all tasks
    @DeleteMapping
    public ResponseEntity<Void> deleteTask() {
        taskService.deleteAllTask();

        return ResponseEntity.noContent().build();
    }

}
