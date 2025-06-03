package com.adrian.Habits.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
