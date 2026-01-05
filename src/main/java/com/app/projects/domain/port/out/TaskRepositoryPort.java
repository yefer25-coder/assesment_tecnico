package com.app.projects.domain.port.out;

import com.app.projects.domain.model.Task;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepositoryPort {
    Task save(Task task);

    Optional<Task> findById(UUID id);

    long countByProjectId(UUID projectId);
}
