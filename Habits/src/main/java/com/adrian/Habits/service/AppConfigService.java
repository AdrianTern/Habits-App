package com.adrian.Habits.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.adrian.Habits.model.AppConfig;
import com.adrian.Habits.repository.AppConfigRepository;
import com.adrian.Habits.repository.TaskRepository;

import jakarta.transaction.Transactional;

@Service
public class AppConfigService{
    private final AppConfigRepository appConfigRepository;
    private final TaskRepository taskRepository;
    private final String cleanUpConfigKey = "cleanup_config";

    public AppConfigService(AppConfigRepository appConfigRepository, TaskRepository taskRepository){
        this.appConfigRepository = appConfigRepository;
        this.taskRepository = taskRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void scheduledCleanup(){
        System.out.println("Executing scheduled cleanup at: " + LocalDateTime.now().toString());
        String logMessage = cleanUpCompletedTasks();
        System.out.println(logMessage);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationStartUp(){
        System.out.println("Executing onApplicationStartUp: ");
        String logMessage = cleanUpCompletedTasks();
        System.out.println(logMessage);
    }

    @Transactional
    public String cleanUpCompletedTasks(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        String currentDateTimeString = currentDateTime.toString();
        Boolean needCleanUp = false;

        // Check if clean up is needed
        AppConfig cleanUpConfig = appConfigRepository.findById(cleanUpConfigKey).orElse(null);
        if (cleanUpConfig == null) {
            cleanUpConfig = new AppConfig(cleanUpConfigKey, currentDateTimeString);
            needCleanUp = true;
        }else{
            LocalDateTime lastCleanUp = LocalDateTime.parse(cleanUpConfig.getConfigValue());
            needCleanUp = Duration.between(lastCleanUp, currentDateTime).compareTo(Duration.ofHours(24)) >= 0;
        }

        // Run the cleanup if the difference between lastCleanup and current datetime >= 24
        if (needCleanUp) {            
            // Delete all completed tasks
            taskRepository.deleteByIsCompleted(true);

            // Update cleanUpConfig
            cleanUpConfig.setConfigValue(currentDateTimeString);
            appConfigRepository.save(cleanUpConfig);

            return "Task clean up executed at: " + currentDateTimeString;
        }
        
        return "Task clean up is not required.";
    }

}