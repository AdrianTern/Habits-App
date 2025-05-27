package com.adrian.Habits.repository;

import com.adrian.Habits.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByTitle(String title);

    List<TaskEntity> findByDueDate(LocalDate dueDate);

    List<TaskEntity> findByDueDateAfter(LocalDate dueDate);

    List<TaskEntity> findByDueDateBefore(LocalDate dueDate);
}

