package com.app.projects.domain.port.out;

import com.app.projects.domain.model.Project;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepositoryPort {
    Project save(Project project);

    Optional<Project> findById(UUID id);
}
