package com.app.projects.infraestructure.adapter.persistence;

import com.app.projects.domain.model.Project;
import com.app.projects.domain.port.out.ProjectRepositoryPort;
import com.app.projects.infraestructure.adapter.persistence.entity.ProjectEntity;
import com.app.projects.infraestructure.adapter.persistence.entity.UserEntity; // Added import for UserEntity
import com.app.projects.infraestructure.adapter.persistence.repository.ProjectJpaRepository;
import com.app.projects.infraestructure.adapter.persistence.repository.UserJpaRepository; // Added import for UserJpaRepository
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectPersistenceAdapter implements ProjectRepositoryPort {

    private final ProjectJpaRepository projectJpaRepository;
    private final UserJpaRepository userJpaRepository; // Need to fetch UserEntity

    @Override
    public Project save(Project project) {
        ProjectEntity entity = toEntity(project);
        ProjectEntity savedEntity = projectJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Project> findById(UUID id) {
        return projectJpaRepository.findById(id).map(this::toDomain);
    }

    private ProjectEntity toEntity(Project project) {
        UserEntity owner = userJpaRepository.findById(project.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + project.getOwnerId()));

        return ProjectEntity.builder()
                .id(project.getId())
                .owner(owner)
                .name(project.getName())
                .status(project.getStatus())
                .deleted(project.isDeleted())
                .build();
    }

    @Override
    public List<Project> findAllByOwnerId(UUID ownerId) {
        return projectJpaRepository.findAllByOwnerId(ownerId).stream()
                .map(this::toDomain)
                .toList();
    }

    private Project toDomain(ProjectEntity entity) {
        return Project.builder()
                .id(entity.getId())
                .ownerId(entity.getOwner().getId())
                .name(entity.getName())
                .status(entity.getStatus())
                .deleted(entity.isDeleted())
                .build();
    }
}
