package com.adrian.Habits.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
// API request file for RoutineDetails, sent from client to be embedded to a TaskEntity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutineDetailsRequest {
    private Boolean isRoutineTask;
}
