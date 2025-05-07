package com.adrian.Habits.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class UpdateTaskRequest {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String description;
    private LocalDate dueDate;
    private Boolean isComplete;

    public String getTitle() {
        return title;
    }

    public Boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Boolean complete) {
        isComplete = complete;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
