package com.portfolio.taskmanager.web.dto;

import com.portfolio.taskmanager.domain.model.TaskStatus;
import jakarta.validation.constraints.Size;

public record TaskUpdateRequest(
        @Size(max = 255) String title,
        @Size(max = 10000) String description,
        TaskStatus status
) {}
