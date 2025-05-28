package com.adrian.Habits.service;

import com.adrian.Habits.dto.CreateTaskRequest;
import com.adrian.Habits.dto.TaskResponse;
import com.adrian.Habits.dto.UpdateTaskRequest;
import com.adrian.Habits.mapper.TaskMapper;
import com.adrian.Habits.model.TaskEntity;
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

    public List<TaskResponse> getAllTask(){
        return taskRepository.findAll()
                             .stream()
                             .map(TaskMapper::toTaskResponse)
                             .toList();
    }

    public TaskResponse getTaskById(Long id){
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        return TaskMapper.toTaskResponse(task);
    }

    public List<TaskResponse> getTaskByTitle(String title){
        return taskRepository.findByTitle(title)
                             .stream()
                             .map(TaskMapper::toTaskResponse)
                             .toList();
    }

    public List<TaskResponse> getTaskByDueDate(LocalDate dueDate){
        return taskRepository.findByDueDate(dueDate)
                             .stream()
                             .map(TaskMapper::toTaskResponse)
                             .toList();
    }

    public List<TaskResponse> getUpcomingTasks(LocalDate dueDate){
        return taskRepository.findByDueDateAfter(dueDate)
                             .stream()
                             .map(TaskMapper::toTaskResponse)
                             .toList();
    }

    public List<TaskResponse> getOverduedTasks(LocalDate dueDate){
        return taskRepository.findByDueDateBeforeAndIsCompleted(dueDate, false)
                             .stream()
                             .map(TaskMapper::toTaskResponse)
                             .toList();
    }

    public List<TaskResponse> getAllTasks(){
        return taskRepository.findAllTasks()
                            .stream()
                            .map(TaskMapper::toTaskResponse)
                            .toList();
    }

    public TaskResponse createTask(CreateTaskRequest createTaskRequest) {
        TaskEntity task = TaskMapper.toTaskEntity(createTaskRequest);
        TaskEntity saved = taskRepository.save(task);
        
        return TaskMapper.toTaskResponse(saved);
    }

    public TaskResponse updateTask(Long id,UpdateTaskRequest updateTaskRequest){
        TaskEntity task = taskRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Task not found"));

        TaskMapper.updateTaskEntity(task, updateTaskRequest);
        TaskEntity saved = taskRepository.save(task);

        return TaskMapper.toTaskResponse(saved);
    }

    public TaskResponse toggleIsComplete(Long id){
        TaskEntity task = taskRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setIsCompleted(!task.getIsCompleted());
        TaskEntity saved = taskRepository.save(task);

        return TaskMapper.toTaskResponse(saved);
    }

    public void deleteTask(Long id){
        TaskEntity task = taskRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.delete(task);
    }

    public void deleteAllTask(){
        taskRepository.deleteAll();
    }
}
