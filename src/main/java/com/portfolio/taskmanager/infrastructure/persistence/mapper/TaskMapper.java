package com.portfolio.taskmanager.infrastructure.persistence.mapper;

import com.portfolio.taskmanager.domain.model.Task;
import com.portfolio.taskmanager.infrastructure.persistence.entity.TaskEntity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public final class TaskMapper {
    private TaskMapper() {}

    public static TaskEntity toEntity(Task task) {
        TaskEntity e = new TaskEntity();
        e.setId(task.getId() != null ? task.getId().toString() : UUID.randomUUID().toString());
        e.setTitle(task.getTitle());
        e.setDescription(task.getDescription());
        e.setStatus(task.getStatus());
        
        OffsetDateTime createdAt = task.getCreatedAt();
        OffsetDateTime updatedAt = task.getUpdatedAt();
        e.setCreatedAt(createdAt != null ? createdAt.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime() : null);
        e.setUpdatedAt(updatedAt != null ? updatedAt.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime() : null);
        e.setUpdatedBy(task.getUpdatedBy());
        return e;
    }

    public static Task toDomain(TaskEntity e) {
        Task t = new Task();
        t.setId(e.getId() != null ? UUID.fromString(e.getId()) : null);
        t.setTitle(e.getTitle());
        t.setDescription(e.getDescription());
        t.setStatus(e.getStatus());
        
        LocalDateTime createdAt = e.getCreatedAt();
        LocalDateTime updatedAt = e.getUpdatedAt();
        t.setCreatedAt(createdAt != null ? createdAt.atOffset(ZoneOffset.UTC) : null);
        t.setUpdatedAt(updatedAt != null ? updatedAt.atOffset(ZoneOffset.UTC) : null);
        t.setUpdatedBy(e.getUpdatedBy());
        return t;
    }
}
