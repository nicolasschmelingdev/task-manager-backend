package com.portfolio.taskmanager.domain.port;

import com.portfolio.taskmanager.domain.model.Task;
import com.portfolio.taskmanager.domain.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepositoryPort {
    Task save(Task task);
    Optional<Task> findById(UUID id);
    void deleteById(UUID id);
    Page<Task> search(String status, String search, Pageable pageable);
}
