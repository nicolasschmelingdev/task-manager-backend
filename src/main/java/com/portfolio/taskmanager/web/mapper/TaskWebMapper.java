package com.portfolio.taskmanager.web.mapper;

import com.portfolio.taskmanager.domain.model.Task;
import com.portfolio.taskmanager.web.dto.TaskCreateRequest;
import com.portfolio.taskmanager.web.dto.TaskResponse;
import com.portfolio.taskmanager.web.dto.TaskUpdateRequest;

public final class TaskWebMapper {
    private TaskWebMapper() {}

    public static Task toDomain(TaskCreateRequest req) {
        Task t = new Task();
        t.setTitle(req.title());
        t.setDescription(req.description());
        t.setStatus(req.status());
        return t;
        }

    public static Task toDomain(TaskUpdateRequest req) {
        Task t = new Task();
        t.setTitle(req.title());
        t.setDescription(req.description());
        t.setStatus(req.status());
        return t;
    }

    public static TaskResponse toResponse(Task t) {
        return new TaskResponse(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getStatus(),
                t.getCreatedAt(),
                t.getUpdatedAt(),
                t.getUpdatedBy()
        );
    }
}
