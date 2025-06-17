package com.adrian.Habits.service;

import com.adrian.Habits.dto.request.CreateTaskRequest;
import com.adrian.Habits.dto.request.UpdateTaskRequest;
import com.adrian.Habits.dto.response.TaskResponse;
import com.adrian.Habits.exception.TaskNotFoundException;
import com.adrian.Habits.mapper.TaskMapper;
import com.adrian.Habits.model.TaskEntity;
import com.adrian.Habits.repository.TaskRepository;
import com.adrian.Habits.specification.TaskSpecificationBuilder;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

//Service layer for TaskEntity to handle CRUD actions
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    // Get all tasks
    public List<TaskResponse> getAllTasks(){
        return taskRepository.findAll()
                             .stream()
                             .map(TaskMapper::toTaskResponse)
                             .toList();
    }

    // Get today tasks
    public List<TaskResponse> getTodayTasks(LocalDate dueDate){
        return taskRepository.findAll(new TaskSpecificationBuilder().withToday(dueDate).build())
                             .stream()
                             .map(TaskMapper::toTaskResponse)
                             .toList();
    }

    // Get upcoming tasks
    public List<TaskResponse> getUpcomingTasks(LocalDate dueDate){
        return taskRepository.findAll(new TaskSpecificationBuilder().withUpcoming(dueDate).build())
                             .stream()
                             .map(TaskMapper::toTaskResponse)
                             .toList();
    }

    // Get overdue tasks
    public List<TaskResponse> getOverdueTasks(LocalDate dueDate){
        return taskRepository.findByDueDateBeforeAndIsCompletedFalse(dueDate)
                             .stream()
                             .map(TaskMapper::toTaskResponse)
                             .toList();
    }

    // Get routine tasks
    public List<TaskResponse> getRoutineTasks(){
        return taskRepository.findByRoutineDetailsIsRoutineTaskTrue()
                            .stream()
                            .map(TaskMapper::toTaskResponse)
                            .toList();
    }

    // Get count of all tasks
    public long getAllTasksCount(){
        return taskRepository.count();
    }

    // Get count of today tasks
    public long getTodayTaskCount(LocalDate dueDate){
        return taskRepository.count(new TaskSpecificationBuilder().withToday(dueDate).build());
    }

    // Get count of upcoming tasks
    public long getUpcomingTasksCount(LocalDate dueDate){
        return taskRepository.count(new TaskSpecificationBuilder().withUpcoming(dueDate).build());
    }

    // Get count of overdue tasks
    public long getOverdueTasksCount(LocalDate dueDate){
        return taskRepository.countByDueDateBeforeAndIsCompletedFalse(dueDate);
    }

    // Get count of routine tasks
    public long getRoutineTasksCount(){
        return taskRepository.countByRoutineDetailsIsRoutineTaskTrue();
    }

    // Create task
    public TaskResponse createTask(CreateTaskRequest createTaskRequest) {
        TaskEntity task = TaskMapper.toTaskEntity(createTaskRequest);
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
