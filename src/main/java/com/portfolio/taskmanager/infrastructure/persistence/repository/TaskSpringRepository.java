package com.portfolio.taskmanager.infrastructure.persistence.repository;

import com.portfolio.taskmanager.infrastructure.persistence.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskSpringRepository extends JpaRepository<TaskEntity, String>, JpaSpecificationExecutor<TaskEntity> {
}
