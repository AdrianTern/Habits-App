package com.adrian.Habits.dto.wrapper;

import java.util.List;

import com.adrian.Habits.dto.TaskCount;
import com.adrian.Habits.dto.response.TaskResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
// A wrapper class to wrap TaskResponse and TaskCount into a single JSON response to client
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponseWrapper {
    private List<TaskResponse> taskResponse;
    private TaskCount taskCount;
}
