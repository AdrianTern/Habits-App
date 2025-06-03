package com.adrian.Habits.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
public class CreateTaskRequest {
    @NotBlank(message = "Title cannot be blank")
    private String title;
    private String description;
    private LocalDate dueDate;
    @JsonProperty("routineDetailsResponse")
    private RoutineDetailsRequest routineDetailsRequest;
}
