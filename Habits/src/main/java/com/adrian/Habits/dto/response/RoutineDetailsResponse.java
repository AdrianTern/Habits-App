package com.adrian.Habits.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
// Response file for RoutineDetails, to be used to communicate with client
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutineDetailsResponse {
    private Boolean isRoutineTask;
}
