package com.portfolio.taskmanager.application.service;

import com.portfolio.taskmanager.domain.model.Task;
import com.portfolio.taskmanager.domain.model.TaskStatus;
import com.portfolio.taskmanager.domain.port.TaskRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepositoryPort repository;

    public TaskService(TaskRepositoryPort repository) {
        this.repository = repository;
    }

    public Page<Task> search(String status, String search, Pageable pageable) {
        return repository.search(status, search, pageable);
    }

    public Task get(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found"));
    }

    public Task create(Task task, String updatedBy) {
        OffsetDateTime now = OffsetDateTime.now();
        task.setId(UUID.randomUUID());
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        task.setUpdatedBy(updatedBy);
        return repository.save(task);
    }

    public Task update(UUID id, Task partial, String updatedBy) {
        Task current = get(id);
        if (partial.getTitle() != null) current.setTitle(partial.getTitle());
        if (partial.getDescription() != null) current.setDescription(partial.getDescription());
        if (partial.getStatus() != null) current.setStatus(partial.getStatus());
        current.setUpdatedAt(OffsetDateTime.now());
        current.setUpdatedBy(updatedBy);
        return repository.save(current);
    }

    public void delete(UUID id) {
        get(id); 
        repository.deleteById(id);
    }
}
