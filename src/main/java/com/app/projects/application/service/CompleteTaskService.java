package com.app.projects.application.service;

import com.app.projects.domain.model.Project;
import com.app.projects.domain.model.Task;
import com.app.projects.domain.port.in.CompleteTaskUseCase;
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
public class CompleteTaskService implements CompleteTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final ProjectRepositoryPort projectRepositoryPort;
    private final CurrentUserPort currentUserPort;
    private final AuditLogPort auditLogPort;
    private final NotificationPort notificationPort;

    @Override
    public void completeTask(UUID taskId) {
        Task task = taskRepositoryPort.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));

        if (task.isCompleted()) {
            throw new IllegalStateException("La tarea ya está completada");
        }

        Project project = projectRepositoryPort.findById(task.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        UUID currentUserId = currentUserPort.getCurrentUserId();
        if (!project.getOwnerId().equals(currentUserId)) {
            throw new SecurityException("Solo el propietario puede modificar la tarea");
        }

        task.setCompleted(true);
        taskRepositoryPort.save(task);

        auditLogPort.register("COMPLETE_TASK", taskId);
        notificationPort.notify("Tarea completada: " + taskId);
    }
}
