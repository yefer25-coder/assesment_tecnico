package com.app.projects.infraestructure.adapter.persistence.repository;

import com.app.projects.infraestructure.adapter.persistence.entity.TaskEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, UUID> {
    long countByProjectId(UUID projectId);
}
