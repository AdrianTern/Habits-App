package com.adrian.Habits.service;

import com.adrian.Habits.dto.CreateTaskRequest;
import com.adrian.Habits.dto.UpdateTaskRequest;
import com.adrian.Habits.model.Task;
import com.adrian.Habits.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTask(){
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id){
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public List<Task> getTaskByTitle(String title){
        return taskRepository.findByTitle(title);
    }

    public List<Task> getTaskByDueDate(LocalDate dueDate){
        return taskRepository.findByDueDate(dueDate);
    }

    public Task createTask(CreateTaskRequest createTaskRequest) {
        return taskRepository.save(new Task(
                createTaskRequest.getTitle(),
                createTaskRequest.getDescription(),
                createTaskRequest.getDueDate()
        ));
    }

    public Task updateTask(Long id,UpdateTaskRequest updateTaskRequest){
        Task task = getTaskById(id);

        if (updateTaskRequest.getTitle() != null) task.setTitle(updateTaskRequest.getTitle());
        if (updateTaskRequest.getDescription() != null) task.setDescription(updateTaskRequest.getDescription());
        if (updateTaskRequest.getDueDate() != null) task.setDueDate(updateTaskRequest.getDueDate());
        task.setIsComplete(updateTaskRequest.getIsComplete());

        return taskRepository.save(task);
    }

    public Task toggleIsComplete(Long id){
        Task task = getTaskById(id);
        task.setIsComplete(!task.getIsComplete());

        return taskRepository.save(task);
    }

    public void deleteTask(Long id){
        Task task = getTaskById(id);

        taskRepository.delete(task);
    }

    public void deleteAllTask(){
        taskRepository.deleteAll();
    }
}
