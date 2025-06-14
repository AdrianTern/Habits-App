package com.adrian.Habits.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// Class to store the number of tasks for each type of tasks, to be wrapped and send to client 
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskCount {
    private long todayCount;
    private long upcomingCount;
    private long overdueCount;
    private long allCount;
    private long routineCount;
}
