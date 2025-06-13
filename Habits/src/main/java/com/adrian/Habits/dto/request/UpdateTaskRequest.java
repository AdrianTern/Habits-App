package com.adrian.Habits.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
// API request to update a task, sent from client to update a TaskEntity
@Data
@Builder
public class UpdateTaskRequest {

    @NotBlank(message = "Title cannot be blank")
    private String title;
    private String description;
    private LocalDate dueDate;
    private Boolean isCompleted;
    @JsonProperty("routineDetailsResponse")
    private RoutineDetailsRequest routineDetailsRequest;
}
