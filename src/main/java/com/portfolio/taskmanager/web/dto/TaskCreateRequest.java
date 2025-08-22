package com.portfolio.taskmanager.web.dto;

import com.portfolio.taskmanager.domain.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskCreateRequest(
        @NotBlank @Size(max = 255) String title,
        @Size(max = 10000) String description,
        @NotNull TaskStatus status
) {}
