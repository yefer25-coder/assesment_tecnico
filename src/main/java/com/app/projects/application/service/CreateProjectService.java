package com.app.projects.application.service;

import com.app.projects.domain.model.Project;
import com.app.projects.domain.port.in.CreateProjectUseCase;
import com.app.projects.domain.port.out.ProjectRepositoryPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateProjectService implements CreateProjectUseCase {

    private final ProjectRepositoryPort projectRepositoryPort;

    @Override
    public Project createProject(UUID ownerId, String name) {
        Project project = Project.builder()
                .id(UUID.randomUUID())
                .ownerId(ownerId)
                .name(name)
                .status(Project.ProjectStatus.DRAFT)
                .deleted(false)
                .build();
        return projectRepositoryPort.save(project);
    }
}
