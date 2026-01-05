package com.app.projects.domain.port.out;

import com.app.projects.domain.model.Project;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepositoryPort {
    Project save(Project project);

    List<Project> findAllByOwnerId(UUID ownerId);

    Optional<Project> findById(UUID id);
}
