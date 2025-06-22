package com.adrian.Habits.controller;

import com.adrian.Habits.dto.request.CreateTaskRequest;
import com.adrian.Habits.dto.request.UpdateTaskRequest;
import com.adrian.Habits.dto.response.TaskResponse;
import com.adrian.Habits.exception.TaskNotFoundException;
import com.adrian.Habits.model.TaskCount;
import com.adrian.Habits.service.TaskService;
import jakarta.validation.Valid;

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
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final Clock clock;

    public TaskController(TaskService taskService, Clock clock) {
        this.taskService = taskService;
        this.clock = clock;
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // Endpoint to get tasks according to selected filter from client
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(@RequestParam String filter,
            @RequestParam(required = false, defaultValue = "Etc/UTC") String timeZone) {
        ZoneId zoneId = ZoneId.of(timeZone);
        LocalDate today = Instant.now(clock).atZone(zoneId).toLocalDate();

        if (filter != null) {
            if ("today".equalsIgnoreCase(filter))
                return ResponseEntity.ok(taskService.getTodayTasks(today));
            else if ("upcoming".equalsIgnoreCase(filter))
                return ResponseEntity.ok(taskService.getUpcomingTasks(today));
            else if ("overdue".equalsIgnoreCase(filter))
                return ResponseEntity.ok(taskService.getOverdueTasks(today));
            else if ("all".equalsIgnoreCase(filter))
                return ResponseEntity.ok(taskService.getAllTasks());
            else if ("routine".equalsIgnoreCase(filter))
                return ResponseEntity.ok(taskService.getRoutineTasks());
        }

        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // Endpoint to get number of tasks for each filter
    @GetMapping("/taskCount")
    public ResponseEntity<TaskCount> getTaskCount(
            @RequestParam(required = false, defaultValue = "Etc/UTC") String timeZone) {
        ZoneId zoneId = ZoneId.of(timeZone);
        LocalDate today = Instant.now(clock).atZone(zoneId).toLocalDate();

        TaskCount taskCount = TaskCount.builder()
                .todayCount(taskService.getTodayTaskCount(today))
                .upcomingCount(taskService.getUpcomingTasksCount(today))
                .overdueCount(taskService.getOverdueTasksCount(today))
                .allCount(taskService.getAllTasksCount())
                .routineCount(taskService.getRoutineTasksCount())
                .build();
        return ResponseEntity.ok(taskCount);
    }

    // Endpoint to create a new task
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskResponse task = taskService.createTask(request);

        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    // Endpoint to update a task
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest request) {
        TaskResponse task = taskService.updateTask(id, request);

        return ResponseEntity.ok(task);
    }

    // Endpoint to toggle completion of a task
    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> toggleTask(@PathVariable Long id) {
        TaskResponse task = taskService.toggleIsComplete(id);

        return ResponseEntity.ok(task);
    }

    // Endpoint to delete a task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
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
