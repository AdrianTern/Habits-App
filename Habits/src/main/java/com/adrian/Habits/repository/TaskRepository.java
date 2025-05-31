package com.adrian.Habits.repository;

import com.adrian.Habits.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByTitle(String title);

    List<TaskEntity> findByDueDateOrDueDateIsNull(LocalDate dueDate);

    List<TaskEntity> findByDueDateAfterOrDueDateIsNull(LocalDate dueDate);

    @Query("SELECT t FROM TaskEntity t WHERE t.dueDate IS NULL OR t.dueDate >= CURRENT_DATE OR (t.dueDate < CURRENT_DATE AND NOT t.isCompleted)")
    List<TaskEntity> findAllTasks();

    List<TaskEntity> findByDueDateBeforeAndIsCompleted(LocalDate dueDate, Boolean isCompleted);

    void deleteByIsCompleted(Boolean isCompleted);
}

