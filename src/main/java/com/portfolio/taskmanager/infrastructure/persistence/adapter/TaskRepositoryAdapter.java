package com.portfolio.taskmanager.infrastructure.persistence.adapter;

import com.portfolio.taskmanager.domain.model.Task;
import com.portfolio.taskmanager.domain.port.TaskRepositoryPort;
import com.portfolio.taskmanager.infrastructure.persistence.entity.TaskEntity;
import com.portfolio.taskmanager.infrastructure.persistence.mapper.TaskMapper;
import com.portfolio.taskmanager.infrastructure.persistence.repository.TaskSpringRepository;
import com.portfolio.taskmanager.infrastructure.persistence.spec.TaskSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class TaskRepositoryAdapter implements TaskRepositoryPort {

    private final TaskSpringRepository repository;

    public TaskRepositoryAdapter(TaskSpringRepository repository) {
        this.repository = repository;
    }

    @Override
    public Task save(Task task) {
        TaskEntity entity = TaskMapper.toEntity(task);
        TaskEntity saved = repository.save(entity);
        return TaskMapper.toDomain(saved);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return repository.findById(id.toString()).map(TaskMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id.toString());
    }

    @Override
    public Page<Task> search(String status, String search, Pageable pageable) {
        Specification<TaskEntity> spec = Specification.where(TaskSpecifications.statusEquals(status))
                .and(TaskSpecifications.titleOrDescriptionContains(search));
        return repository.findAll(spec, pageable).map(TaskMapper::toDomain);
    }
}
