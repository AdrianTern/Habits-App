package com.adrian.Habits.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class RoutineDetails {
    @Column(nullable = false)
    @Builder.Default
    private Boolean isRoutineTask = false;
}
