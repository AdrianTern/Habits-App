package com.adrian.Habits.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.adrian.Habits.model.TaskEntity;

// Builder class to combine specifications, to be used as query in service layer
public class TaskSpecificationBuilder {

    private Specification<TaskEntity> spec;

    public TaskSpecificationBuilder() {
        this.spec = Specification.where(null);
    }

    // Select tasks with the following criteria:
    // dueDate = null
    // OR dueDate = today
    // OR valid routine task
    public TaskSpecificationBuilder withToday(LocalDate dueDate) {
        if (dueDate != null) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.isNull(root.get("dueDate")),
                    cb.equal(root.get("dueDate"), dueDate),
                    cb.and(
                            cb.isNotNull(root.get("routineDetails")),
                            cb.equal(root.get("routineDetails").get("isRoutineTask"), true),
                            cb.greaterThanOrEqualTo(root.get("dueDate"), dueDate))));
        }
        return this;
    }

    // Select tasks with the following criteria:
    // NOT routine task
    // AND (dueDate = null OR dueDate > today)
    public TaskSpecificationBuilder withUpcoming(LocalDate dueDate) {
        if (dueDate != null) {
            spec = spec.and((root, query, cb) -> cb.and(
                cb.or(
                    cb.isNull(root.get("routineDetails")),
                    cb.equal(root.get("routineDetails").get("isRoutineTask"), false)),
                cb.or(
                    cb.isNull(root.get("dueDate")),
                    cb.greaterThan(root.get("dueDate"), dueDate))));
        }
        return this;
    }

    public Specification<TaskEntity> build() {
        return spec;
    }
}
