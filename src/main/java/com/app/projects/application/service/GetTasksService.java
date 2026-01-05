package com.app.projects.application.service;

import com.app.projects.domain.model.Project;
import com.app.projects.domain.model.Task;
import com.app.projects.domain.port.in.GetTasksUseCase;
import com.app.projects.domain.port.out.CurrentUserPort;
import com.app.projects.domain.port.out.ProjectRepositoryPort;
import com.app.projects.domain.port.out.TaskRepositoryPort;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTasksService implements GetTasksUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final ProjectRepositoryPort projectRepositoryPort;
    private final CurrentUserPort currentUserPort;

    @Override
    public List<Task> getTasks(UUID projectId) {
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        UUID currentUserId = currentUserPort.getCurrentUserId();
        if (!project.getOwnerId().equals(currentUserId)) {
            throw new SecurityException("You do not have permission to view tasks for this project");
        }

        return taskRepositoryPort.findAllByProjectId(projectId);
    }
}
