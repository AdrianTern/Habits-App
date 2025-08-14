package com.adrian.Habits.service;

import com.adrian.Habits.dto.request.CreateTaskRequest;
import com.adrian.Habits.dto.request.UpdateTaskRequest;
import com.adrian.Habits.dto.response.TaskResponse;
import com.adrian.Habits.exception.TaskNotFoundException;
import com.adrian.Habits.exception.UserNotFoundException;
import com.adrian.Habits.mapper.TaskMapper;
import com.adrian.Habits.model.TaskEntity;
import com.adrian.Habits.model.UserEntity;
import com.adrian.Habits.repository.TaskRepository;
import com.adrian.Habits.repository.UserRepository;
import com.adrian.Habits.specification.TaskSpecificationBuilder;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

//Service layer for TaskEntity to handle CRUD actions
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository){
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // Get all tasks
    public List<TaskResponse> getAllTasks(){
        return taskRepository.findAll()
                             .stream()
                             .map(TaskMapper::toTaskResponse)
                             .toList();
    }


    // Get all tasks by userId
    public List<TaskResponse> getAllTasksByUser(Long userId){
        return taskRepository.findByUserId(userId)
                             .stream()
                             .map(TaskMapper::toTaskResponse)
                             .toList();
    }

    // Get today tasks
    public List<TaskResponse> getTodayTasksByUser(Long userId, LocalDate dueDate){
        return taskRepository.findAll(new TaskSpecificationBuilder().withToday(userId, dueDate).build())
                             .stream()
                             .map(TaskMapper::toTaskResponse)
                             .toList();
    }

    // Get upcoming tasks
    public List<TaskResponse> getUpcomingTasksByUser(Long userId, LocalDate dueDate){
        return taskRepository.findAll(new TaskSpecificationBuilder().withUpcoming(userId, dueDate).build())
                             .stream()
                             .map(TaskMapper::toTaskResponse)
                             .toList();
    }

    // Get overdue tasks
    public List<TaskResponse> getOverdueTasksByUser(Long userId, LocalDate dueDate){
        return taskRepository.findByUserIdAndDueDateBeforeAndIsCompletedFalse(userId, dueDate)
                             .stream()
                             .map(TaskMapper::toTaskResponse)
                             .toList();
    }

    // Get routine tasks
    public List<TaskResponse> getRoutineTasksByUser(Long userId){
        return taskRepository.findByUserIdAndRoutineDetailsIsRoutineTaskTrue(userId)
                            .stream()
                            .map(TaskMapper::toTaskResponse)
                            .toList();
    }

    // Get count of all tasks
    public long getAllTasksCountByUser(Long userId){
        return taskRepository.countByUserId(userId);
    }

    // Get count of today tasks
    public long getTodayTaskCountByUser(Long userId, LocalDate dueDate){
        return taskRepository.count(new TaskSpecificationBuilder().withToday(userId, dueDate).build());
    }

    // Get count of upcoming tasks
    public long getUpcomingTasksCountByUser(Long userId, LocalDate dueDate){
        return taskRepository.count(new TaskSpecificationBuilder().withUpcoming(userId, dueDate).build());
    }

    // Get count of overdue tasks
    public long getOverdueTasksCountByUser(Long userId, LocalDate dueDate){
        return taskRepository.countByUserIdAndDueDateBeforeAndIsCompletedFalse(userId, dueDate);
    }

    // Get count of routine tasks
    public long getRoutineTasksCountByUser(Long userId){
        return taskRepository.countByUserIdAndRoutineDetailsIsRoutineTaskTrue(userId);
    }

    // Create task
    public TaskResponse createTask(CreateTaskRequest createTaskRequest, Long userId) {
        UserEntity user = userRepository.findById(userId)
                                        .orElseThrow(() -> new UserNotFoundException("User not found"));

        TaskEntity task = TaskMapper.toTaskEntity(createTaskRequest, user);
        TaskEntity saved = taskRepository.save(task);
        
        return TaskMapper.toTaskResponse(saved);
    }

    // Update task
    public TaskResponse updateTask(Long id, UpdateTaskRequest updateTaskRequest){
        TaskEntity task = taskRepository.findById(id)
                                        .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        TaskMapper.updateTaskEntity(task, updateTaskRequest);
        TaskEntity saved = taskRepository.save(task);

        return TaskMapper.toTaskResponse(saved);
    }

    // Toggle isComplete (patch)
    public TaskResponse toggleIsComplete(Long id){
        TaskEntity task = taskRepository.findById(id)
                                        .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        task.setIsCompleted(!task.getIsCompleted());
        TaskEntity saved = taskRepository.save(task);

        return TaskMapper.toTaskResponse(saved);
    }

    // Delete task
    public void deleteTask(Long id){
        TaskEntity task = taskRepository.findById(id)
                                        .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        taskRepository.delete(task);
    }

    // Delete all tasks
    public void deleteAllTask(){
        taskRepository.deleteAll();
    }
}
