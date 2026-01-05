package com.app.projects.application.service;

import com.app.projects.domain.model.Project;
import com.app.projects.domain.port.in.GetProjectsUseCase;
import com.app.projects.domain.port.out.ProjectRepositoryPort;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProjectsService implements GetProjectsUseCase {

    private final ProjectRepositoryPort projectRepositoryPort;

    @Override
    public List<Project> getProjects(UUID ownerId) {
        return projectRepositoryPort.findAllByOwnerId(ownerId);
    }
}
