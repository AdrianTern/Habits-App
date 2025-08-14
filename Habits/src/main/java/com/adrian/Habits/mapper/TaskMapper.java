package com.adrian.Habits.mapper;

import com.adrian.Habits.dto.request.CreateTaskRequest;
import com.adrian.Habits.dto.request.RoutineDetailsRequest;
import com.adrian.Habits.dto.request.UpdateTaskRequest;
import com.adrian.Habits.dto.response.RoutineDetailsResponse;
import com.adrian.Habits.dto.response.TaskResponse;
import com.adrian.Habits.dto.response.UserResponse;
import com.adrian.Habits.model.RoutineDetails;
import com.adrian.Habits.model.TaskEntity;
import com.adrian.Habits.model.UserEntity;
// Mapper class to convert task response and task request to task entity, or the other way round
public class TaskMapper {

    // Convert routine details request from client to routine details model
    public static RoutineDetails toRoutineDetails(RoutineDetailsRequest request){
        if (request != null) return new RoutineDetails(request.getIsRoutineTask());
        return null;
    }

    // Convert routine details model from server to client as response
    public static RoutineDetailsResponse toRoutineDetailsResponse(RoutineDetails routineDetails){
        if (routineDetails != null) return new RoutineDetailsResponse(routineDetails.getIsRoutineTask());  
        return null;
    }

    // Convert UserEntity to UserResponse
    public static UserResponse toUserResponse(UserEntity user) {
        if (user != null) return new UserResponse(user.getId(), user.getUsername());
        return null;
    }
    
    // Convert task entity from server to client as response
    public static TaskResponse toTaskResponse(TaskEntity task){
        return TaskResponse.builder()
                           .id(task.getId())
                           .title(task.getTitle())
                           .description(task.getDescription())
                           .dueDate((task.getDueDate() != null) ? task.getDueDate().toString(): null)
                           .isCompleted(task.getIsCompleted()) 
                           .routineDetailsResponse(toRoutineDetailsResponse(task.getRoutineDetails()))
                           .userResponse(toUserResponse(task.getUser()))
                           .build();
    }

    // Convert task request from client to task entity
    public static TaskEntity toTaskEntity(CreateTaskRequest request, UserEntity user){
        return TaskEntity.builder()
                         .title(request.getTitle())
                         .description(request.getDescription())
                         .dueDate(request.getDueDate())
                         .routineDetails(toRoutineDetails(request.getRoutineDetailsRequest()))
                         .user(user)
                         .build();
    }

    // Update task entity with request from client
    public static void updateTaskEntity(TaskEntity task, UpdateTaskRequest request){
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setIsCompleted(request.getIsCompleted());  
        task.setRoutineDetails(toRoutineDetails(request.getRoutineDetailsRequest()));
    }
}
