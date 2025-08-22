package com.portfolio.taskmanager.web.dto;

import com.portfolio.taskmanager.domain.model.TaskStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        TaskStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        String updatedBy
) {}
