package com.portfolio.taskmanager.web.mapper;

import com.portfolio.taskmanager.domain.model.Task;
import com.portfolio.taskmanager.web.dto.TaskCreateRequest;
import com.portfolio.taskmanager.web.dto.TaskResponse;
import com.portfolio.taskmanager.web.dto.TaskUpdateRequest;

public final class TaskWebMapper {
    private TaskWebMapper() {}

    public static Task toDomain(TaskCreateRequest req) {
        Task t = new Task();
        t.setTitle(req.getTitle());
        t.setDescription(req.getDescription());
        t.setStatus(req.getStatus());
        return t;
        }

    public static Task toDomain(TaskUpdateRequest req) {
        Task t = new Task();
        t.setTitle(req.getTitle());
        t.setDescription(req.getDescription());
        t.setStatus(req.getStatus());
        return t;
    }

    public static TaskResponse toResponse(Task t) {
        TaskResponse r = new TaskResponse();
        r.setId(t.getId());
        r.setTitle(t.getTitle());
        r.setDescription(t.getDescription());
        r.setStatus(t.getStatus());
        r.setCreatedAt(t.getCreatedAt());
        r.setUpdatedAt(t.getUpdatedAt());
        r.setUpdatedBy(t.getUpdatedBy());
        return r;
    }
}
