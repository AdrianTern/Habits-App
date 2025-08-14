package com.adrian.Habits.utils;

import java.time.LocalDate;

import com.adrian.Habits.model.RoutineDetails;
import com.adrian.Habits.model.TaskEntity;
import com.adrian.Habits.model.UserEntity;

// Builder class to create TaskEntity
public class MockTaskBuilder {
    private String title = "mock";
    private LocalDate dueDate = LocalDate.of(2025, 5, 5);
    private Boolean isCompleted = false;
    private Boolean isRoutineTask = false;
    private UserEntity user = new MockUserBuilder().withUsername("admin").withPassword("admin123").build();

    public MockTaskBuilder withTitle(String title){
        this.title = title;
        return this;
    }

    public MockTaskBuilder withDueDate(LocalDate dueDate){
        this.dueDate = dueDate;
        return this;
    }

    public MockTaskBuilder withIsCompleted(Boolean isCompleted){
        this.isCompleted = isCompleted;
        return this;
    }

    public MockTaskBuilder withIsRoutineTask(Boolean isRoutineTask){
        this.isRoutineTask = isRoutineTask;
        return this;
    }

    public MockTaskBuilder withUser(UserEntity user){
        this.user = user;
        return this;
    }

    public TaskEntity build(){
        return TaskEntity.builder()
                        .title(title)
                        .dueDate(dueDate)
                        .isCompleted(isCompleted)
                        .routineDetails(new RoutineDetails(isRoutineTask))
                        .user(user)
                        .build();
    }

    
}
