package com.app.projects.application.service;

import com.app.projects.domain.model.Project;
import com.app.projects.domain.port.in.ActivateProjectUseCase;
import com.app.projects.domain.port.out.AuditLogPort;
import com.app.projects.domain.port.out.CurrentUserPort;
import com.app.projects.domain.port.out.NotificationPort;
import com.app.projects.domain.port.out.ProjectRepositoryPort;
import com.app.projects.domain.port.out.TaskRepositoryPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivateProjectService implements ActivateProjectUseCase {

    private final ProjectRepositoryPort projectRepositoryPort;
    private final TaskRepositoryPort taskRepositoryPort;
    private final CurrentUserPort currentUserPort;
    private final AuditLogPort auditLogPort;
    private final NotificationPort notificationPort;

    @Override
    public void activateProject(UUID projectId) {
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        UUID currentUserId = currentUserPort.getCurrentUserId();
        if (!project.getOwnerId().equals(currentUserId)) {
            throw new SecurityException("Solo el propietario puede modificar el proyecto");
        }

        long taskCount = taskRepositoryPort.countByProjectId(projectId);
        if (taskCount == 0) {
            throw new IllegalStateException("El proyecto debe tener al menos una tarea para ser activado");
        }

        project.setStatus(Project.ProjectStatus.ACTIVE);
        projectRepositoryPort.save(project);

        auditLogPort.register("ACTIVATE_PROJECT", projectId);
        notificationPort.notify("Proyecto activado: " + projectId);
    }
}
