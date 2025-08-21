package com.adrian.Habits.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

import com.adrian.Habits.utils.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
// API request to update a task, sent from client to update a TaskEntity
@Data
@Builder
public class UpdateTaskRequest {

    @NotBlank(message = Constants.EXCEPTION_INVALID_TITLE)
    private String title;
    private String description;
    private LocalDate dueDate;
    private Boolean isCompleted;
    @JsonProperty(Constants.JSON_ROUTINE_DETAILS)
    private RoutineDetailsRequest routineDetailsRequest;
}
