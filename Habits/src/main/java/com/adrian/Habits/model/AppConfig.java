package com.adrian.Habits.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
// A model class to indicate different kinds of configuration for the app.
// 1. cleanup_config
// 2. resetRoutine_config
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppConfig {
    @Id
    private String configKey;
    private String configValue;
}
