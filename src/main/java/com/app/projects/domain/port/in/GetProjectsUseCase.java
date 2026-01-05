package com.app.projects.domain.port.in;

import com.app.projects.domain.model.Project;
import java.util.List;
import java.util.UUID;

public interface GetProjectsUseCase {
    List<Project> getProjects(UUID ownerId);
}
