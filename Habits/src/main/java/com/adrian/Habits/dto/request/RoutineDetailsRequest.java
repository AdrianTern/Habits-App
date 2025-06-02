package com.adrian.Habits.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutineDetailsRequest {
    private Boolean isRoutineTask;
    private LocalDate routineEndDate;
}
