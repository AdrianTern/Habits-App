package com.adrian.Habits.controller;

import com.adrian.Habits.dto.CreateTaskRequest;
import com.adrian.Habits.dto.TaskCount;
import com.adrian.Habits.dto.TaskResponse;
import com.adrian.Habits.dto.TaskResponseWrapper;
import com.adrian.Habits.dto.UpdateTaskRequest;
import com.adrian.Habits.service.TaskService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<TaskResponseWrapper> getAllTask(@RequestParam(required = false) String filter,
                                                         @RequestParam(required = false, defaultValue = "Etc/UTC") String timezone){
        ZoneId zoneid = ZoneId.of(timezone);
        LocalDate today = LocalDate.now(zoneid);

        List<TaskResponse> todayTasks = taskService.getTaskByDueDate(today);
        List<TaskResponse> upcomingTasks = taskService.getUpcomingTasks(today);
        List<TaskResponse> overdueTasks = taskService.getOverduedTasks(today);
        List<TaskResponse> allTasks = taskService.getAllTasks();

        TaskCount taskCount = new TaskCount(todayTasks.size(), upcomingTasks.size(), overdueTasks.size(), allTasks.size());
                                    
        TaskResponseWrapper tasksResponseWrapper = TaskResponseWrapper.builder().taskCount(taskCount).build();

        if(filter!=null){
            if ("today".equalsIgnoreCase(filter)) {
                tasksResponseWrapper.setTaskResponse(todayTasks);
            } else if("upcoming".equalsIgnoreCase(filter)) {
                tasksResponseWrapper.setTaskResponse(upcomingTasks);
            } else if("overdue".equalsIgnoreCase(filter)){
                tasksResponseWrapper.setTaskResponse(overdueTasks);
            }
        } else{
            tasksResponseWrapper.setTaskResponse(allTasks);
        }

        return ResponseEntity.ok(tasksResponseWrapper);
    }

    @GetMapping("/{id}/get-by-id")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        TaskResponse task = taskService.getTaskById(id);

        return (task != null) ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{title}/get-by-title")
    public ResponseEntity<List<TaskResponse>> getTaskByTitle(@PathVariable String title) {
        List<TaskResponse> tasks = taskService.getTaskByTitle(title);

        return (!tasks.isEmpty()) ? ResponseEntity.ok(tasks) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{dueDate}/get-by-dueDate")
    public ResponseEntity<List<TaskResponse>> getTaskByDueDate(@PathVariable LocalDate dueDate) {
        List<TaskResponse> tasks = taskService.getTaskByDueDate(dueDate);

        return (!tasks.isEmpty()) ? ResponseEntity.ok(tasks) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskResponse task = taskService.createTask(request);

        return (task != null) ? new ResponseEntity<>(task, HttpStatus.CREATED) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest request) {
        TaskResponse task = taskService.updateTask(id, request);

        return (task != null) ? ResponseEntity.ok(task) : ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> toggleTask(@PathVariable Long id) {
        TaskResponse task = taskService.toggleIsComplete(id);

        return (task != null) ? ResponseEntity.ok(task) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTask() {
        taskService.deleteAllTask();

        return ResponseEntity.noContent().build();
    }

}
