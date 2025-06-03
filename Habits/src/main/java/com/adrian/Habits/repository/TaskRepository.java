package com.adrian.Habits.repository;

import com.adrian.Habits.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByTitle(String title);

    // All tasks
    @Query("""
            SELECT t FROM TaskEntity t 
            WHERE t.dueDate IS NULL 
            OR t.dueDate >= :dueDate 
            OR (t.dueDate < :dueDate AND NOT t.isCompleted)
            """)
    List<TaskEntity> findAllTasks(@Param("dueDate") LocalDate dueDate);

    // Today's tasks
    @Query("""
            SELECT t from TaskEntity t 
            WHERE t.dueDate IS NULL 
            OR t.dueDate = :dueDate 
            OR (t.routineDetails.isRoutineTask = true 
                AND t.dueDate >= :dueDate)
            """)
    List<TaskEntity> findTodayTasks(@Param("dueDate") LocalDate dueDate);
    
    // Upcoming tasks
    List<TaskEntity> findByDueDateAfterOrDueDateIsNull(LocalDate dueDate);

    // Overdue tasks
    List<TaskEntity> findByDueDateBeforeAndIsCompleted(LocalDate dueDate, Boolean isCompleted);

    // Routines
    List<TaskEntity> findByRoutineDetailsIsRoutineTaskTrue();

    // Delete completed tasks
    void deleteByIsCompleted(Boolean isCompleted); 
}

