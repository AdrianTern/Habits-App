package com.adrian.Habits.mapper;

import com.adrian.Habits.dto.CreateTaskRequest;
import com.adrian.Habits.dto.TaskResponse;
import com.adrian.Habits.dto.UpdateTaskRequest;
import com.adrian.Habits.model.TaskEntity;

public class TaskMapper {
    
    public static TaskResponse toTaskResponse(TaskEntity task){
        return TaskResponse.builder()
                           .id(task.getId())
                           .title(task.getTitle())
                           .description(task.getDescription())
                           .dueDate((task.getDueDate() != null) ? task.getDueDate().toString(): null)
                           .isComplete(task.getIsComplete())
                           .build();
    }

    public static TaskEntity toTaskEntity(CreateTaskRequest request){
        return TaskEntity.builder()
                         .title(request.getTitle())
                         .description(request.getDescription())
                         .dueDate(request.getDueDate())
                         .build();
    }

    public static void updateTaskEntity(TaskEntity task, UpdateTaskRequest request){
        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        task.setIsComplete(request.getIsComplete());
    }
}
