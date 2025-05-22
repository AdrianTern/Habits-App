package com.adrian.Habits.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UpdateTaskRequest {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String description;
    private LocalDate dueDate;
    private Boolean isCompleted;
}
