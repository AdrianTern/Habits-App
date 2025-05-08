package com.adrian.Habits.controller;

import com.adrian.Habits.dto.CreateTaskRequest;
import com.adrian.Habits.dto.UpdateTaskRequest;
import com.adrian.Habits.model.Task;
import com.adrian.Habits.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTask(){
        List<Task> tasks = taskService.getAllTask();

        return (!tasks.isEmpty()) ? ResponseEntity.ok(tasks) : ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/get-by-id")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id){
        Task task = taskService.getTaskById(id);

        return (task != null) ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{title}/get-by-title")
    public ResponseEntity<List<Task>> getTaskByTitle(@PathVariable String title){
        List<Task> tasks = taskService.getTaskByTitle(title);

        return (!tasks.isEmpty()) ? ResponseEntity.ok(tasks) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{dueDate}/get-by-dueDate")
    public ResponseEntity<List<Task>> getTaskByDueDate(@PathVariable LocalDate dueDate){
        List<Task> tasks = taskService.getTaskByDueDate(dueDate);

        return (!tasks.isEmpty()) ? ResponseEntity.ok(tasks) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest request){
        Task task = taskService.createTask(request);

        return (task != null) ? new ResponseEntity<>(task, HttpStatus.CREATED) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest request){
        Task task = taskService.updateTask(id, request);

        return (task != null) ? ResponseEntity.ok(task) : ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{id}/toggle-completion")
    public ResponseEntity<Task> toggleTask(@PathVariable Long id){
        Task task = taskService.toggleIsComplete(id);

        return (task != null) ? ResponseEntity.ok(task) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTask(){
        taskService.deleteAllTask();

        return ResponseEntity.noContent().build();
    }

}
