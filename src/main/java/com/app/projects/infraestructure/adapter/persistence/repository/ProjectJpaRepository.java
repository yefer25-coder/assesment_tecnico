package com.app.projects.infraestructure.adapter.persistence.repository;

import com.app.projects.infraestructure.adapter.persistence.entity.ProjectEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectJpaRepository extends JpaRepository<ProjectEntity, UUID> {
    List<ProjectEntity> findAllByOwnerId(UUID ownerId);
}
