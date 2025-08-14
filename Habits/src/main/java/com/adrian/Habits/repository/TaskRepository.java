package com.adrian.Habits.repository;

import com.adrian.Habits.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
// Repository interface for TaskEntity
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {
    
    // Get all tasks
    List<TaskEntity> findByUserId(Long userId);

    // Get overdue tasks
    List<TaskEntity> findByUserIdAndDueDateBeforeAndIsCompletedFalse(Long userId, LocalDate dueDate);

    // Get Routines
    List<TaskEntity> findByUserIdAndRoutineDetailsIsRoutineTaskTrue(Long userId);

    // Get all routines
    List<TaskEntity> findByRoutineDetailsIsRoutineTaskTrue();

    // Delete completed tasks
    void deleteByIsCompleted(Boolean isCompleted); 

    // Count all tasks
    long countByUserId(Long userId);

    // Count overdue tasks
    long countByUserIdAndDueDateBeforeAndIsCompletedFalse(Long userId, LocalDate dueDate);

    // Count routines
    long countByUserIdAndRoutineDetailsIsRoutineTaskTrue(Long userId);
}

