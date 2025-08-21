package com.adrian.Habits.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.adrian.Habits.model.AppConfig;
import com.adrian.Habits.model.TaskEntity;
import com.adrian.Habits.repository.AppConfigRepository;
import com.adrian.Habits.repository.TaskRepository;
import com.adrian.Habits.utils.Constants;

import org.springframework.transaction.annotation.Transactional;

// Service class to handle scheduled daemons and onStartUp
// 1. Clean up completed tasks
// 2. Reset completed routine tasks and delete expired routines
@Service
public class AppConfigService {
    private final AppConfigRepository appConfigRepository;
    private final TaskRepository taskRepository;

    private final String cleanUpConfigKey = Constants.CONFIG_KEY_CLEANUP;
    private final String resetRoutineConfigKey = Constants.CONFIG_KEY_RESET_ROUTINE;

    private final LocalDateTime currenDateTime = LocalDateTime.now();
    private final LocalDate currentDate = currenDateTime.toLocalDate();
    private final String currenDateTimeString = currenDateTime.toString();

    public AppConfigService(AppConfigRepository appConfigRepository, TaskRepository taskRepository) {
        this.appConfigRepository = appConfigRepository;
        this.taskRepository = taskRepository;
    }

    // Find/Create cleanUpConfig
    public AppConfig getCleanUpConfig() {
        // Get the app config row
        AppConfig cleanUpConfig = appConfigRepository.findById(cleanUpConfigKey).orElse(null);

        // Create a new row if the row does not exists and save to repo
        if (cleanUpConfig == null) {
            cleanUpConfig = AppConfig.builder().configKey(cleanUpConfigKey).build();
            appConfigRepository.save(cleanUpConfig);
        }
        return cleanUpConfig;
    }

    // Find/Create resetRoutineConfig
    public AppConfig getResetRoutineConfig() {
        // Get the app config row
        AppConfig resetRoutineConfig = appConfigRepository.findById(resetRoutineConfigKey).orElse(null);

        // Create a new row if the row does not exists and save to repo
        if (resetRoutineConfig == null) {
            resetRoutineConfig = AppConfig.builder().configKey(resetRoutineConfigKey).build();
            appConfigRepository.save(resetRoutineConfig);
        }
        return resetRoutineConfig;
    }

    // Scheduled method that runs daily every midnight 12AM
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void dailyRun() {
        System.out.println("Executing scheduled daily run at: " + LocalDateTime.now().toString());
        runAppConfigActions(true);
    }

    // Method that runs on application start up
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationStartUp() {
        System.out.println("Executing onApplicationStartUp: ");
        runAppConfigActions(false);
    }

    // Method that runs all config actions
    @Transactional
    public void runAppConfigActions(Boolean isScheduled) {
        // Reset routine tasks
        try {
            if (isScheduled) {
                resetRoutineTasks(getResetRoutineConfig());
            } else {
                resetRoutineTasks_onApplicationStartup(getResetRoutineConfig());
            }
        } catch (Exception e) {
            System.err.println("Failed to reset routine tasks: " + e.getMessage());
        }

        // Clean up completed tasks
        try {
            if (isScheduled) {
                cleanUpCompletedTasks(getCleanUpConfig());
            } else {
                cleanUpCompletedTasks_onApplicationStartup(getCleanUpConfig());
            }
        } catch (Exception e) {
            System.err.println("Failed to clean up completed tasks: " + e.getMessage());
        }
    }

    // Clean up completed tasks
    @Transactional
    public void cleanUpCompletedTasks(AppConfig cleanUpConfig) {
        // Delete all completed tasks
        taskRepository.deleteByIsCompleted(true);

        // Save the new run time
        cleanUpConfig.setConfigValue(currenDateTimeString);

        System.out.println("Task clean up executed at: " + currenDateTimeString);
    }

    // Clean up completed tasks on application startup
    @Transactional
    public void cleanUpCompletedTasks_onApplicationStartup(AppConfig cleanUpConfig) {
        if (cleanUpConfig != null) {
             // Run the cleanup if the difference between lastCleanup and current datetime >= 24
            LocalDateTime lastExecutedTime = LocalDateTime.parse(cleanUpConfig.getConfigValue());
            Boolean isRunRequired = Duration.between(lastExecutedTime, currenDateTime)
                    .compareTo(Duration.ofHours(24)) >= 0;

            if (isRunRequired) {
                cleanUpCompletedTasks(cleanUpConfig);
            } else
                System.out
                        .println("Task clean up is not required. Last executed at: " + cleanUpConfig.getConfigValue());
        }
    }

    // Reset completed routine tasks and delete expired routines
    @Transactional
    public void resetRoutineTasks(AppConfig resetRoutineConfig) {
        // Get all routine tasks and check if the routine is still valid till today
        // Delete the routine if it is expired, else reset isCompleted
        List<TaskEntity> routineTasks = taskRepository.findByRoutineDetailsIsRoutineTaskTrue();
        routineTasks.forEach(routine -> {
            LocalDate endDate = routine.getDueDate();
            if (endDate != null && endDate.isBefore(currentDate))
                taskRepository.delete(routine);
            else
                routine.setIsCompleted(false);
        });

        // Save the new run time
        resetRoutineConfig.setConfigValue(currenDateTimeString);

        System.out.println("Routine tasks resetted at: " + currenDateTimeString);
    }

    // Reset completed routine tasks and delete expired routines on application
    // startup
    @Transactional
    public void resetRoutineTasks_onApplicationStartup(AppConfig resetRoutineConfig) {
        if (resetRoutineConfig != null) {
             // Run the cleanup if the difference between lastCleanup and current datetime >= 24
            LocalDateTime lastExecutedTime = LocalDateTime.parse(resetRoutineConfig.getConfigValue());
            Boolean isRunRequired = Duration.between(lastExecutedTime, currenDateTime)
                    .compareTo(Duration.ofHours(24)) >= 0;

            if (isRunRequired) {
                resetRoutineTasks(resetRoutineConfig);
            } else
                System.out.println(
                        "Routine tasks reset is not required. Last executed at: "
                                + resetRoutineConfig.getConfigValue());
        }
    }
}