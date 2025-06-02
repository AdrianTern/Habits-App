package com.adrian.Habits.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutineDetailsResponse {
    private Boolean isRoutineTask;
    private String routineEndDate;
}
