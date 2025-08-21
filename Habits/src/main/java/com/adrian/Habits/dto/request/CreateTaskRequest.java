package com.adrian.Habits.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

import com.adrian.Habits.utils.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
// API request to create a task, sent from client to create a TaskEntity
@Data
@Builder
public class CreateTaskRequest {
    @NotBlank(message = Constants.EXCEPTION_INVALID_TITLE)
    private String title;
    private String description;
    private LocalDate dueDate;
    @JsonProperty(Constants.JSON_ROUTINE_DETAILS)
    private RoutineDetailsRequest routineDetailsRequest;
}
