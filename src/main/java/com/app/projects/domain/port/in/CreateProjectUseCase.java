package com.app.projects.domain.port.in;

import com.app.projects.domain.model.Project;
import java.util.UUID;

public interface CreateProjectUseCase {
    Project createProject(UUID ownerId, String name);
}
