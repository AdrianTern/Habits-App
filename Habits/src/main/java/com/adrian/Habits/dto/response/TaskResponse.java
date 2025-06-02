package com.adrian.Habits.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String dueDate;
    private Boolean isCompleted;
    private RoutineDetailsResponse routineDetailsResponse;
}
