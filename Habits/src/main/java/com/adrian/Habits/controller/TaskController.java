package com.adrian.Habits.controller;

import com.adrian.Habits.dto.TaskCount;
import com.adrian.Habits.dto.request.CreateTaskRequest;
import com.adrian.Habits.dto.request.UpdateTaskRequest;
import com.adrian.Habits.dto.response.TaskResponse;
import com.adrian.Habits.dto.wrapper.TaskResponseWrapper;
import com.adrian.Habits.service.TaskService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
// Controller class that handles the endpoints for CRUD actions
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Endpoint to get tasks according to selected filter from client
    @GetMapping
    public ResponseEntity<TaskResponseWrapper> getTasks(@RequestParam String filter,
            @RequestParam(required = false, defaultValue = "Etc/UTC") String timezone) {
        ZoneId zoneid = ZoneId.of(timezone);
        LocalDate today = LocalDate.now(zoneid);

        TaskCount taskCount = TaskCount.builder()
                .todayCount(taskService.getTodayTaskCount(today))
                .upcomingCount(taskService.getUpcomingTasksCount(today))
                .overdueCount(taskService.getOverdueTasksCount(today))
                .allCount(taskService.getAllTasksCount())
                .routineCount(taskService.getRoutineTasksCount())
                .build();

        TaskResponseWrapper tasksResponseWrapper = TaskResponseWrapper.builder().taskCount(taskCount).build();

        if (filter != null) {
            if ("today".equalsIgnoreCase(filter)) {
                tasksResponseWrapper.setTaskResponse(taskService.getTodayTasks(today));
            } else if ("upcoming".equalsIgnoreCase(filter)) {
                tasksResponseWrapper.setTaskResponse(taskService.getUpcomingTasks(today));
            } else if ("overdue".equalsIgnoreCase(filter)) {
                tasksResponseWrapper.setTaskResponse(taskService.getOverdueTasks(today));
            } else if ("all".equalsIgnoreCase(filter)) {
                tasksResponseWrapper.setTaskResponse(taskService.getAllTasks());
            } else if ("routine".equalsIgnoreCase(filter)) {
                tasksResponseWrapper.setTaskResponse(taskService.getRoutineTasks());
            }
        }

        return ResponseEntity.ok(tasksResponseWrapper);
    }

    // Endpoint to create a new task
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskResponse task = taskService.createTask(request);

        return (task != null) ? new ResponseEntity<>(task, HttpStatus.CREATED) : ResponseEntity.badRequest().build();
    }

    // Endpoint to update a task
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest request) {
        TaskResponse task = taskService.updateTask(id, request);

        return (task != null) ? ResponseEntity.ok(task) : ResponseEntity.badRequest().build();
    }

    // Endpoint to toggle completion of a task
    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> toggleTask(@PathVariable Long id) {
        TaskResponse task = taskService.toggleIsComplete(id);

        return (task != null) ? ResponseEntity.ok(task) : ResponseEntity.badRequest().build();
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
