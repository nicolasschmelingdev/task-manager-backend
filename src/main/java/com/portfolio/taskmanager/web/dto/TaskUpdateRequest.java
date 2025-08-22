package com.portfolio.taskmanager.web.dto;

import com.portfolio.taskmanager.domain.model.TaskStatus;
import jakarta.validation.constraints.Size;

public class TaskUpdateRequest {

    @Size(max = 255)
    private String title;

    @Size(max = 10000)
    private String description;

    private TaskStatus status;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
}
