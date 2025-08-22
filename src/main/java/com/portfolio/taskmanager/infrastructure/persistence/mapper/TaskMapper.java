package com.portfolio.taskmanager.infrastructure.persistence.mapper;

import com.portfolio.taskmanager.domain.model.Task;
import com.portfolio.taskmanager.infrastructure.persistence.entity.TaskEntity;

import java.util.UUID;

public final class TaskMapper {
    private TaskMapper() {}

    public static TaskEntity toEntity(Task task) {
        TaskEntity e = new TaskEntity();
        e.setId(task.getId() != null ? task.getId().toString() : UUID.randomUUID().toString());
        e.setTitle(task.getTitle());
        e.setDescription(task.getDescription());
        e.setStatus(task.getStatus());
        e.setCreatedAt(task.getCreatedAt());
        e.setUpdatedAt(task.getUpdatedAt());
        e.setUpdatedBy(task.getUpdatedBy());
        return e;
    }

    public static Task toDomain(TaskEntity e) {
        Task t = new Task();
        t.setId(e.getId() != null ? UUID.fromString(e.getId()) : null);
        t.setTitle(e.getTitle());
        t.setDescription(e.getDescription());
        t.setStatus(e.getStatus());
        t.setCreatedAt(e.getCreatedAt());
        t.setUpdatedAt(e.getUpdatedAt());
        t.setUpdatedBy(e.getUpdatedBy());
        return t;
    }
}
