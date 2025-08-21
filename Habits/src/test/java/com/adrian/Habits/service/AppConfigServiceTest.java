package com.adrian.Habits.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.adrian.Habits.dto.response.TaskResponse;
import com.adrian.Habits.model.AppConfig;
import com.adrian.Habits.model.UserEntity;
import com.adrian.Habits.repository.TaskRepository;
import com.adrian.Habits.repository.UserRepository;
import com.adrian.Habits.utils.Constants;
import com.adrian.Habits.utils.MockMethods;
import com.adrian.Habits.utils.MockTaskBuilder;

// Integration tests for AppConfigService
@SpringBootTest
@Transactional
public class AppConfigServiceTest {
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppConfigService appConfigService;

    @Autowired
    private TaskService taskService;

    private final String cleanUpConfigKey = Constants.CONFIG_KEY_CLEANUP;
    private final String resetRoutineConfigKey = Constants.CONFIG_KEY_RESET_ROUTINE;
    private final LocalDate currentDate = LocalDate.now();
    private final LocalDate prevDate = currentDate.minusDays(2);
    private final LocalDateTime currentDateTime = LocalDateTime.now();
    private final LocalDateTime prevDateTime = currentDateTime.minusDays(2);
    private final String username = "admin";
    private final String password = "Admin123!";

    @Test
    public void getCleanUpConfig_shouldReturnCleanUpConfig(){
        AppConfig result = appConfigService.getCleanUpConfig();
        
        assertNotNull(result);
        assertEquals(cleanUpConfigKey, result.getConfigKey());
    }

    @Test
    public void getResetRoutineConfig_shouldReturnResetRoutineConfig(){
        AppConfig result = appConfigService.getResetRoutineConfig();
        
        assertNotNull(result);
        assertEquals(resetRoutineConfigKey, result.getConfigKey());
    }

    @Test
    public void runCleanUpConfig_shouldCleanUpCompletedTasks(){
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        // Create a incompleted task
        taskRepository.save(new MockTaskBuilder().withUser(user).build());
        // Create a completed task
        taskRepository.save(new MockTaskBuilder().withIsCompleted(true).withUser(user).build());
        taskRepository.flush();

        // Run cleanUpCompletedTasks
        AppConfig cleanUpConfig = appConfigService.getCleanUpConfig();
        appConfigService.cleanUpCompletedTasks(cleanUpConfig);

        List<TaskResponse> result = taskService.getAllTasks();
        
        // Check cleanUpConfig is updated with the new timestamp
        // Check completed task is deleted
        assertNotNull(cleanUpConfig);
        assertEquals(1, result.size());
        assertEquals(false, result.get(0).getIsCompleted());
        assertEquals(currentDate, LocalDateTime.parse(cleanUpConfig.getConfigValue()).toLocalDate());
    }

    @Test
    public void cleanUpCompletedTasks_onApplicationStartUp_shouldNotRun() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        // Create a incompleted task
        taskRepository.save(new MockTaskBuilder().withUser(user).build());
        // Create a completed task
        taskRepository.save(new MockTaskBuilder().withIsCompleted(true).withUser(user).build());
        taskRepository.flush();

        AppConfig cleanUpConfig = AppConfig.builder()
                                        .configKey(cleanUpConfigKey)
                                        .configValue(currentDateTime.toString())
                                        .build();
        appConfigService.cleanUpCompletedTasks_onApplicationStartup(cleanUpConfig);

       List<TaskResponse> result = taskService.getAllTasks();
        
       // Check no tasks are deleted
       // Check the completed task still exists
       // Check the timestamp is not updated
       assertNotNull(cleanUpConfig);
       assertEquals(2, result.size());
       assertTrue(result.stream().anyMatch(task -> task.getIsCompleted() == true));
       assertEquals(currentDateTime.toString(), cleanUpConfig.getConfigValue());
    }

    @Test
    public void cleanUpCompletedTasks_onApplicationStartUp_shouldRun() {
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        // Create a incompleted task
        taskRepository.save(new MockTaskBuilder().withUser(user).build());
        // Create a completed task
        taskRepository.save(new MockTaskBuilder().withIsCompleted(true).withUser(user).build());
        taskRepository.flush();

        AppConfig cleanUpConfig = appConfigService.getCleanUpConfig();
        cleanUpConfig.setConfigValue(prevDateTime.toString());
        appConfigService.cleanUpCompletedTasks_onApplicationStartup(cleanUpConfig);

       List<TaskResponse> result = taskService.getAllTasks();
        
        // Check cleanUpConfig is updated with the new timestamp
        // Check completed task is deleted
        assertNotNull(cleanUpConfig);
        assertEquals(1, result.size());
        assertEquals(false, result.get(0).getIsCompleted());
        assertEquals(currentDate, LocalDateTime.parse(cleanUpConfig.getConfigValue()).toLocalDate());
    }

    @Test
    public void runresetRoutineConfig_shouldResetCompletedRoutinesAndDeleteExpiredRoutines(){
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        // Create a completed normal task within due date
        taskRepository.save(new MockTaskBuilder().withDueDate(currentDate).withIsCompleted(true).withUser(user).build());
        // Create an overdue normal task
        taskRepository.save(new MockTaskBuilder().withDueDate(prevDate).withUser(user).build());
        // Create an incomplete routine task within due date
        taskRepository.save(new MockTaskBuilder().withDueDate(currentDate).withIsRoutineTask(true).withUser(user).build());
        // Create a completed routine task within due date
        taskRepository.save(new MockTaskBuilder().withDueDate(currentDate).withIsCompleted(true).withIsRoutineTask(true).withUser(user).build());
        // Create an expired routine task
        taskRepository.save(new MockTaskBuilder().withDueDate(prevDate).withIsCompleted(true).withIsRoutineTask(true).withUser(user).build());
        taskRepository.flush();

        // Run resetRoutineTasks
        AppConfig resetRoutineConfig = appConfigService.getResetRoutineConfig();
        appConfigService.resetRoutineTasks(resetRoutineConfig);

        List<TaskResponse> result = taskService.getAllTasks();
        
        // Check resetRoutineConfig timestamp is updated
        // Check the expired routine task is deleted
        // Check completed routine task is resetted
        assertNotNull(resetRoutineConfig);
        assertEquals(4, result.size());
        assertTrue(result.stream().noneMatch(task -> task.getIsCompleted() && task.getRoutineDetailsResponse().getIsRoutineTask()));
        assertTrue(result.stream().noneMatch(task -> task.getDueDate().equals(prevDate.toString()) && task.getRoutineDetailsResponse().getIsRoutineTask() ));
        assertEquals(currentDate, LocalDateTime.parse(resetRoutineConfig.getConfigValue()).toLocalDate());
    }

    @Test
    public void resetRoutineTasks_onApplicationStartUp_shouldNotRun(){
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        // Create a completed normal task within due date
        taskRepository.save(new MockTaskBuilder().withDueDate(currentDate).withIsCompleted(true).withUser(user).build());
        // Create an overdue normal task
        taskRepository.save(new MockTaskBuilder().withDueDate(prevDate).withUser(user).build());
        // Create an incomplete routine task within due date
        taskRepository.save(new MockTaskBuilder().withDueDate(currentDate).withIsRoutineTask(true).withUser(user).build());
        // Create a completed routine task within due date
        taskRepository.save(new MockTaskBuilder().withDueDate(currentDate).withIsCompleted(true).withIsRoutineTask(true).withUser(user).build());
        // Create an expired routine task
        taskRepository.save(new MockTaskBuilder().withDueDate(prevDate).withIsCompleted(true).withIsRoutineTask(true).withUser(user).build());
        taskRepository.flush();

        // Run resetRoutineTasks
        AppConfig resetRoutineConfig = AppConfig.builder()
                                                .configKey(resetRoutineConfigKey)
                                                .configValue(currentDateTime.toString())
                                                .build();
        appConfigService.resetRoutineTasks_onApplicationStartup(resetRoutineConfig);

        List<TaskResponse> result = taskService.getAllTasks();
        
        // Check resetRoutineConfig timestamp is not updated
        // Check the expired routine task is not deleted
        // Check completed routine task is not resetted
        assertNotNull(resetRoutineConfig);
        assertEquals(5, result.size());
        assertTrue(result.stream().anyMatch(task -> task.getIsCompleted() && task.getRoutineDetailsResponse().getIsRoutineTask()));
        assertTrue(result.stream().anyMatch(task -> task.getDueDate().equals(prevDate.toString()) && task.getRoutineDetailsResponse().getIsRoutineTask()));
        assertEquals(currentDateTime.toString(), resetRoutineConfig.getConfigValue());
    }

    @Test
    public void resetRoutineTasks_onApplicationStartUp_shouldRun(){
        UserEntity user = MockMethods.mockUser(userRepository, username, password);
        // Create a completed normal task within due date
        taskRepository.save(new MockTaskBuilder().withDueDate(currentDate).withIsCompleted(true).withUser(user).build());
        // Create an overdue normal task
        taskRepository.save(new MockTaskBuilder().withDueDate(prevDate).withUser(user).build());
        // Create an incomplete routine task within due date
        taskRepository.save(new MockTaskBuilder().withDueDate(currentDate).withIsRoutineTask(true).withUser(user).build());
        // Create a completed routine task within due date
        taskRepository.save(new MockTaskBuilder().withDueDate(currentDate).withIsCompleted(true).withIsRoutineTask(true).withUser(user).build());
        // Create an expired routine task
        taskRepository.save(new MockTaskBuilder().withDueDate(prevDate).withIsCompleted(true).withIsRoutineTask(true).withUser(user).build());
        taskRepository.flush();

        // Run resetRoutineTasks
        AppConfig resetRoutineConfig = appConfigService.getResetRoutineConfig();
        resetRoutineConfig.setConfigValue(prevDateTime.toString());
        appConfigService.resetRoutineTasks_onApplicationStartup(resetRoutineConfig);

        List<TaskResponse> result = taskService.getAllTasks();
        
        // Check resetRoutineConfig timestamp is updated
        // Check the expired routine task is deleted
        // Check completed routine task is resetted
        assertNotNull(resetRoutineConfig);
        assertEquals(4, result.size());
        assertTrue(result.stream().noneMatch(task -> task.getIsCompleted() && task.getRoutineDetailsResponse().getIsRoutineTask()));
        assertTrue(result.stream().noneMatch(task -> task.getDueDate().equals(prevDate.toString()) && task.getRoutineDetailsResponse().getIsRoutineTask() ));
        assertEquals(currentDate, LocalDateTime.parse(resetRoutineConfig.getConfigValue()).toLocalDate());
    }
}
