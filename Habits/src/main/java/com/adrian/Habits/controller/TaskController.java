package com.adrian.Habits.controller;

import com.adrian.Habits.dto.CreateTaskRequest;
import com.adrian.Habits.dto.TaskResponse;
import com.adrian.Habits.dto.UpdateTaskRequest;
import com.adrian.Habits.service.TaskService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTask(@RequestParam(required = false) String dueDate){
        List<TaskResponse> tasks = new ArrayList<>();

        if(dueDate!=null){
            LocalDate today = LocalDate.now();
            
            if ("today".equalsIgnoreCase(dueDate)) {
                tasks = taskService.getTaskByDueDate(today);
            } else if("upcoming".equalsIgnoreCase(dueDate)) {
                tasks = taskService.getUpcomingTasks(today);
            } else if("overdued".equalsIgnoreCase(dueDate)){
                tasks = taskService.getOverduedTasks(today);
            }
        } else{
            tasks = taskService.getAllTask();
        }

        return (!tasks.isEmpty()) ? ResponseEntity.ok(tasks) : ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/get-by-id")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id){
        TaskResponse task = taskService.getTaskById(id);

        return (task != null) ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{title}/get-by-title")
    public ResponseEntity<List<TaskResponse>> getTaskByTitle(@PathVariable String title){
        List<TaskResponse> tasks = taskService.getTaskByTitle(title);

        return (!tasks.isEmpty()) ? ResponseEntity.ok(tasks) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{dueDate}/get-by-dueDate")
    public ResponseEntity<List<TaskResponse>> getTaskByDueDate(@PathVariable LocalDate dueDate){
        List<TaskResponse> tasks = taskService.getTaskByDueDate(dueDate);

        return (!tasks.isEmpty()) ? ResponseEntity.ok(tasks) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request){
        TaskResponse task = taskService.createTask(request);

        return (task != null) ? new ResponseEntity<>(task, HttpStatus.CREATED) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest request){
        TaskResponse task = taskService.updateTask(id, request);

        return (task != null) ? ResponseEntity.ok(task) : ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> toggleTask(@PathVariable Long id){
        TaskResponse task = taskService.toggleIsComplete(id);

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
