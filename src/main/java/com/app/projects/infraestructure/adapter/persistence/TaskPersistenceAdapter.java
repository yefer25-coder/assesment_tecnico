package com.app.projects.infraestructure.adapter.persistence;

import com.app.projects.domain.model.Task;
import com.app.projects.domain.port.out.TaskRepositoryPort;
import com.app.projects.infraestructure.adapter.persistence.entity.ProjectEntity;
import com.app.projects.infraestructure.adapter.persistence.entity.TaskEntity;
import com.app.projects.infraestructure.adapter.persistence.repository.ProjectJpaRepository;
import com.app.projects.infraestructure.adapter.persistence.repository.TaskJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements TaskRepositoryPort {

    private final TaskJpaRepository taskJpaRepository;
    private final ProjectJpaRepository projectJpaRepository; // Need to fetch ProjectEntity

    @Override
    public Task save(Task task) {
        return toDomain(taskJpaRepository.save(toEntity(task)));
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return taskJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public long countByProjectId(UUID projectId) {
        // Since we changed to entity relationship, we need to ensure the repository
        // method works or update it.
        // JPA repository usually handles 'findByProjectId' if 'project' is the field
        // name, but 'countByProject_Id' or 'countByProjectId' might need adjustment if
        // using object.
        // Actually 'countByProjectId' works if there is a field 'project' with 'id'.
        // Spring Data JPA is smart.
        // But let's verify TaskJpaRepository.
        return taskJpaRepository.countByProjectId(projectId);
    }

    @Override
    public List<Task> findAllByProjectId(UUID projectId) {
        return taskJpaRepository.findAllByProjectId(projectId).stream()
                .map(this::toDomain)
                .toList();
    }

    private TaskEntity toEntity(Task task) {
        ProjectEntity project = projectJpaRepository.findById(task.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + task.getProjectId()));

        return TaskEntity.builder()
                .id(task.getId())
                .project(project)
                .title(task.getTitle())
                .completed(task.isCompleted())
                .deleted(task.isDeleted())
                .build();
    }

    private Task toDomain(TaskEntity entity) {
        return Task.builder()
                .id(entity.getId())
                .projectId(entity.getProject().getId())
                .title(entity.getTitle())
                .completed(entity.isCompleted())
                .deleted(entity.isDeleted())
                .build();
    }
}
