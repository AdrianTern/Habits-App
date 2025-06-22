package com.adrian.Habits.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import com.adrian.Habits.dto.request.RoutineDetailsRequest;
import com.adrian.Habits.model.RoutineDetails;
import com.adrian.Habits.repository.TaskRepository;
import com.adrian.Habits.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.adrian.Habits.dto.response.TaskResponse;

// Collection of methods used in test cases
public class MockMethods {
    // Mock routine details
    public static RoutineDetails mockRoutineDetails(Boolean isRoutineTask) {
        return new RoutineDetails(isRoutineTask);
    }

    // Mock routine details request
    public static RoutineDetailsRequest mockRoutineDetailsRequest(Boolean isRoutineTask) {
        return new RoutineDetailsRequest(isRoutineTask);
    }

    // Create all tasks
    public static void mockAllTasks(TaskRepository taskRepository, LocalDate dueDate) {
        taskRepository.save(new MockTaskBuilder().withDueDate(dueDate).build());
        taskRepository.save(new MockTaskBuilder().withDueDate(dueDate).build());
        taskRepository.flush();
    }

    // Convert JSON string into type
    public static <T> T parseJson(ObjectMapper objectMapper, String json, TypeReference<T> typeRef) {
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    // Create today tasks
    public static void mockTodayTasks(TaskRepository taskRepository, LocalDate dueDate) {
         // Create task without due date (TRUE)
         taskRepository.save(new MockTaskBuilder().withTitle("no due date").withDueDate(null).build());
         // Create task with due date = today (TRUE)
         taskRepository.save(new MockTaskBuilder().withTitle("due date is tdoay").withDueDate(dueDate).build());
         // Create routine task with future due date (TRUE)
         taskRepository.save(new MockTaskBuilder().withTitle("routine task with future due date").withDueDate(dueDate.plusDays(1)).withIsRoutineTask(true).build());
         // Create routine task with expired due date (FALSE)
         taskRepository.save(new MockTaskBuilder().withTitle("routine with expired").withDueDate(dueDate.minusDays(1)).withIsRoutineTask(true).build());
         // Create task with previous due date (FALSE)
         taskRepository.save(new MockTaskBuilder().withTitle("task with previous").withDueDate(dueDate.minusDays(1)).build());
         // Create future task (FALSE)
         taskRepository.save(new MockTaskBuilder().withTitle("task with future").withDueDate(dueDate.plusDays(1)).build());
         taskRepository.flush();
    }

    // Create upcoming tasks
    public static void mockUpcomingTasks(TaskRepository taskRepository, LocalDate dueDate) {
        // Create future task (TRUE)
        taskRepository.save(new MockTaskBuilder().withDueDate(dueDate.plusDays(1)).build());
        // Create task with no due date (TRUE)
        taskRepository.save(new MockTaskBuilder().withDueDate(null).build());
        // Create task with due date = today (FALSE)
        taskRepository.save(new MockTaskBuilder().withDueDate(dueDate).build());
        // Create task with due date = previous date (FALSE)
        taskRepository.save(new MockTaskBuilder().withDueDate(dueDate.minusDays(1)).build());
        // Create routine task with no due date (FALSE)
        taskRepository.save(new MockTaskBuilder().withDueDate(null).withIsRoutineTask(true).build());
        // Create routine task with future due date (FALSE)
        taskRepository.save(new MockTaskBuilder().withDueDate(dueDate.plusDays(1)).withIsRoutineTask(true).build());
        taskRepository.flush();
    }

    // Create overdue tasks
    public static void mockOverdueTasks(TaskRepository taskRepository, LocalDate dueDate) {
        // Create incomplete task with due date = previous date (TRUE)
        taskRepository.save(new MockTaskBuilder().withDueDate(dueDate.minusDays(1)).build());
        // Create completed task with due date = previous date (FALSE)
        taskRepository.save(new MockTaskBuilder().withDueDate(dueDate.minusDays(1)).withIsCompleted(true).build());
        // Create future incomplete task (FALSE)
        taskRepository.save(new MockTaskBuilder().withDueDate(dueDate.plusDays(1)).build());
        // Create task with due date = today (FALSE)
        taskRepository.save(new MockTaskBuilder().withDueDate(dueDate).build());
        // Create task with no due date (FALSE)
        taskRepository.save(new MockTaskBuilder().withDueDate(null).build());
        taskRepository.flush();
    }

    // Create routine tasks
    public static void mockRoutineTasks(TaskRepository taskRepository) {
        // Create routine task (TRUE)
        taskRepository.save(new MockTaskBuilder().withIsRoutineTask(true).build());
        // Create normal task (FALSE)
        taskRepository.save(new MockTaskBuilder().build());
        taskRepository.flush();
    }

    // Assertion on all asks
    public static void assertOnAllTasks(List<TaskResponse> result) {
        assertEquals(2, result.size());
    }
    
    // Assertion on today tasks
    public static void assertOnTodayTasks(List<TaskResponse> result, LocalDate dueDate) {
        // Check task due date is null 
        // or due date = today
        // or routine task with future due date
        boolean isValid = result.stream().allMatch(task -> task.getDueDate() == null ||
                task.getDueDate().equals(dueDate.toString()) ||
                (task.getDueDate().equals(dueDate.plusDays(1).toString())
                        && task.getRoutineDetailsResponse().getIsRoutineTask() == true));

        assertEquals(3, result.size());
        assertTrue(isValid, "Returned tasks consist of invalid dates");
    }

    // Assertion on upcoming tasks
    public static void assertOnUpcomingTasks(List<TaskResponse> result, LocalDate dueDate) {
        // Check task due date is null
        // or task due date = future date
        boolean isValid = result.stream().allMatch(task -> 
            task.getDueDate() == null ||
            task.getDueDate().equals(dueDate.plusDays(1).toString())
        );

        assertEquals(2, result.size());
        assertTrue(isValid, "Returned tasks consist of invalid dates");
    }

    // Assertion on overdue tasks
    public static void assertOnOverdueTasks(List<TaskResponse> result, LocalDate dueDate) {
        // Check task due date is in the past
        // and task is incompleted
        boolean isValid = result.stream().allMatch(task -> 
            task.getDueDate().equals(dueDate.minusDays(1).toString()) &&
            task.getIsCompleted().equals(false)
        );

        assertEquals(1, result.size());
        assertTrue(isValid, "Returned tasks consist of invalid dates and completion status");
    }

    // Assertion on routine tasks
    public static void assertOnRoutineTasks(List<TaskResponse> result) {
        // Check task due date is in the past
        // and task is incompleted
        boolean isValid = result.stream().allMatch(task -> 
            task.getRoutineDetailsResponse().getIsRoutineTask().equals(true)
        );

        assertEquals(1, result.size());
        assertTrue(isValid, "Returned tasks is not a routine task");
    }
}
