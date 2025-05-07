package com.adrian.Habits.repository;

import com.adrian.Habits.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByTitle(String title);

    List<Task> findByDueDate(LocalDate dueDate);
}

