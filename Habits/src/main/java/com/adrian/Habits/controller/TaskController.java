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
    public List<Task> getALlTask(){
        return taskService.getAllTask();
    }

    @GetMapping("/{id}/get-by-id")
    public Task getTaskById(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @GetMapping("/{title}/get-by-title")
    public List<Task> getTaskByTitle(@PathVariable String title){
        return taskService.getTaskByTitle(title);
    }

    @GetMapping("/{dueDate}/get-by-duedate")
    public List<Task> getTaskByDueDate(@PathVariable LocalDate dueDate){
        return taskService.getTaskByDueDate(dueDate);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest request){
        Task task = taskService.createTask(request);

        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update-task")
    public Task updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest request){
        return taskService.updateTask(id, request);
    }

    @PatchMapping("/{id}/toggle-completion")
    public Task toggleTask(@PathVariable Long id){
        return taskService.toggleIsComplete(id);
    }

    @DeleteMapping("/{id}/delete-task")
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }

    @DeleteMapping
    public void deleteTask(){
        taskService.deleteAllTask();
    }

}
