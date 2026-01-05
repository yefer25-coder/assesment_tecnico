package com.app.projects.infraestructure.adapter.persistence.repository;

import com.app.projects.infraestructure.adapter.persistence.entity.TaskEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, UUID> {
    long countByProjectId(UUID projectId);

    List<TaskEntity> findAllByProjectId(UUID projectId);
}
