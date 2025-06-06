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

import jakarta.transaction.Transactional;

@Service
public class AppConfigService{
    private final AppConfigRepository appConfigRepository;
    private final TaskRepository taskRepository;
    private final String cleanUpConfigKey = "cleanup_config";
    private final String resetRoutineConfigKey = "resetRoutine_config";
    private final LocalDateTime currenDateTime = LocalDateTime.now();
    private final LocalDate currentDate = currenDateTime.toLocalDate();
    private final String currenDateTimeString = currenDateTime.toString();

    public AppConfigService(AppConfigRepository appConfigRepository, TaskRepository taskRepository){
        this.appConfigRepository = appConfigRepository;
        this.taskRepository = taskRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void scheduledCleanup(){
        System.out.println("Executing scheduled cleanup at: " + LocalDateTime.now().toString());
        runAppConfigActions(true);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationStartUp(){
        System.out.println("Executing onApplicationStartUp: ");
        runAppConfigActions(false);
    }

    @Transactional
    public void runAppConfigActions(Boolean isScheduled){
        // Reset routine tasks
        try{
            resetRoutineTasks(isScheduled);
        }catch(Exception e){
            System.err.println("Failed to reset routine tasks: " + e.getMessage());
        }

        // Clean up completed tasks
        try{
            cleanUpCompletedTasks(isScheduled);
        }catch(Exception e){
            System.err.println("Failed to clean up completed tasks: " +e.getMessage());
        }
    }

    @Transactional
    public void cleanUpCompletedTasks(Boolean isScheduled){
        Boolean isRunRequired = isScheduled;
        Boolean isNewConfig = false;

        // Get the app config row
        AppConfig cleanUpConfig = appConfigRepository.findById(cleanUpConfigKey).orElse(null);

        // Create a new row if the row does not exists and save to repo
        if (cleanUpConfig == null) {
            cleanUpConfig = new AppConfig(cleanUpConfigKey, currenDateTimeString);
            appConfigRepository.save(cleanUpConfig);
            isRunRequired = true;
            isNewConfig = true;
        }

        // Check if run is needed on startup
        if (!isScheduled && !isNewConfig) { 
            // Run the cleanup if the difference between lastCleanup and current datetime >= 24
            LocalDateTime lastExecutedTime = LocalDateTime.parse(cleanUpConfig.getConfigValue());
            isRunRequired = Duration.between(lastExecutedTime, currenDateTime).compareTo(Duration.ofHours(24)) >= 0;
        }

        if (isRunRequired ) {            
            // Delete all completed tasks
            taskRepository.deleteByIsCompleted(true);

            // Save the new run time
            if(!isNewConfig) cleanUpConfig.setConfigValue(currenDateTimeString);

            System.out.println("Task clean up executed at: " + currenDateTimeString);
        }
        else System.out.println("Task clean up is not required. Last executed at: " + cleanUpConfig.getConfigValue());
    }

    @Transactional
    public void resetRoutineTasks(Boolean isScheduled){

        Boolean isRunRequired = isScheduled;
        Boolean isNewConfig = false;
        
        //Get the app config row
        AppConfig resetRoutineConfig = appConfigRepository.findById(resetRoutineConfigKey).orElse(null);
 
        // Create a new row if the row does not exists and save to repo
        if (resetRoutineConfig == null) {
            resetRoutineConfig = new AppConfig(resetRoutineConfigKey, currenDateTimeString);
            appConfigRepository.save(resetRoutineConfig);
            isRunRequired = true;
            isNewConfig = true;
        }

        // Check if run is needed on startup
        if (!isScheduled && !isNewConfig) {
            // Run the cleanup if the difference between lastCleanup and current datetime >= 24
            LocalDateTime lastExecutedTime = LocalDateTime.parse(resetRoutineConfig.getConfigValue());
            isRunRequired = Duration.between(lastExecutedTime, currenDateTime).compareTo(Duration.ofHours(24)) >= 0;
        }

        if (isRunRequired) {
            // Get all routine tasks and check if the routine is still valid till today
            // Delete the routine if it is expired, else reset isCompleted
            List<TaskEntity> routineTasks = taskRepository.findByRoutineDetailsIsRoutineTaskTrue();
            routineTasks.forEach(routine -> {
                LocalDate endDate = routine.getDueDate();
                if (endDate != null && endDate.isBefore(currentDate)) taskRepository.delete(routine);
                else routine.setIsCompleted(false);
            });

            // Save the new run time
            if (!isNewConfig) resetRoutineConfig.setConfigValue(currenDateTimeString);

            System.out.println("Routine tasks resetted at: " + currenDateTimeString);
        }
        else System.out.println("Routine tasks reset is not required. Last executed at: " + resetRoutineConfig.getConfigValue());
     }
}