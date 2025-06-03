package com.adrian.Habits.mapper;

import com.adrian.Habits.dto.request.CreateTaskRequest;
import com.adrian.Habits.dto.request.RoutineDetailsRequest;
import com.adrian.Habits.dto.request.UpdateTaskRequest;
import com.adrian.Habits.dto.response.RoutineDetailsResponse;
import com.adrian.Habits.dto.response.TaskResponse;
import com.adrian.Habits.model.RoutineDetails;
import com.adrian.Habits.model.TaskEntity;

public class TaskMapper {
    public static RoutineDetails toRoutineDetails(RoutineDetailsRequest request){
        if (request != null) return new RoutineDetails(request.getIsRoutineTask());
        return null;
    }

    public static RoutineDetailsResponse toRoutineDetailsResponse(RoutineDetails routineDetails){
        if (routineDetails != null) return new RoutineDetailsResponse(routineDetails.getIsRoutineTask());  
        return null;
    }
    
    public static TaskResponse toTaskResponse(TaskEntity task){
        return TaskResponse.builder()
                           .id(task.getId())
                           .title(task.getTitle())
                           .description(task.getDescription())
                           .dueDate((task.getDueDate() != null) ? task.getDueDate().toString(): null)
                           .isCompleted(task.getIsCompleted()) 
                           .routineDetailsResponse(toRoutineDetailsResponse(task.getRoutineDetails()))
                           .build();
    }

    public static TaskEntity toTaskEntity(CreateTaskRequest request){
        return TaskEntity.builder()
                         .title(request.getTitle())
                         .description(request.getDescription())
                         .dueDate(request.getDueDate())
                         .routineDetails(toRoutineDetails(request.getRoutineDetailsRequest()))
                         .build();
    }

    public static void updateTaskEntity(TaskEntity task, UpdateTaskRequest request){
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setIsCompleted(request.getIsCompleted());  
        task.setRoutineDetails(toRoutineDetails(request.getRoutineDetailsRequest()));
    }
}
