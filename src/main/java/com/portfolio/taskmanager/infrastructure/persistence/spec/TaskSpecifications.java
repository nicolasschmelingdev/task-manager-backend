package com.portfolio.taskmanager.infrastructure.persistence.spec;

import com.portfolio.taskmanager.domain.model.TaskStatus;
import com.portfolio.taskmanager.infrastructure.persistence.entity.TaskEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public final class TaskSpecifications {
    private TaskSpecifications() {}

    public static Specification<TaskEntity> statusEquals(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isBlank()) return cb.conjunction();
            try {
                TaskStatus enumStatus = TaskStatus.valueOf(status.toUpperCase());
                return cb.equal(root.get("status"), enumStatus);
            } catch (IllegalArgumentException ex) {
                
                return cb.disjunction();
            }
        };
    }

    public static Specification<TaskEntity> titleOrDescriptionContains(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return cb.conjunction();
            String pattern = "%" + search.trim().toLowerCase() + "%";
            // Use DBMS_LOB.SUBSTR and force String by concatenating with empty string
            Predicate title = cb.like(
                    cb.function(
                            "LOWER",
                            String.class,
                            cb.concat(
                                    cb.function(
                                            "DBMS_LOB.SUBSTR",
                                            String.class,
                                            root.get("title"),
                                            cb.literal(4000),
                                            cb.literal(1)
                                    ),
                                    ""
                            )
                    ),
                    pattern
            );
            
            Predicate description = cb.like(
                    cb.function(
                            "LOWER",
                            String.class,
                            cb.concat(
                                    cb.function(
                                            "DBMS_LOB.SUBSTR",
                                            String.class,
                                            root.get("description"),
                                            cb.literal(4000),
                                            cb.literal(1)
                                    ),
                                    ""
                            )
                    ),
                    pattern
            );
            return cb.or(title, description);
        };
    }
}
