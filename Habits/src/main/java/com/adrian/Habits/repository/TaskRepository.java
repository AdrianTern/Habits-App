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
    
    // Get overdue tasks
    List<TaskEntity> findByDueDateBeforeAndIsCompletedFalse(LocalDate dueDate);

    // Get Routines
    List<TaskEntity> findByRoutineDetailsIsRoutineTaskTrue();

    // Delete completed tasks
    void deleteByIsCompleted(Boolean isCompleted); 

    // Count overdue tasks
    long countByDueDateBeforeAndIsCompletedFalse(LocalDate dueDate);

    // Count routines
    long countByRoutineDetailsIsRoutineTaskTrue();
}

