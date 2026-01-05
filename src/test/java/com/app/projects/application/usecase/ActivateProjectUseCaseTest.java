package com.app.projects.application.usecase;

import com.app.projects.application.service.ActivateProjectService;
import com.app.projects.domain.model.Project;
import com.app.projects.domain.port.out.AuditLogPort;
import com.app.projects.domain.port.out.CurrentUserPort;
import com.app.projects.domain.port.out.NotificationPort;
import com.app.projects.domain.port.out.ProjectRepositoryPort;
import com.app.projects.domain.port.out.TaskRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivateProjectUseCaseTest {

    @Mock
    private ProjectRepositoryPort projectRepositoryPort;
    @Mock
    private TaskRepositoryPort taskRepositoryPort;
    @Mock
    private CurrentUserPort currentUserPort;
    @Mock
    private AuditLogPort auditLogPort;
    @Mock
    private NotificationPort notificationPort;

    @InjectMocks
    private ActivateProjectService activateProjectUseCase;

    @Test
    void ActivateProject_WithTasks_ShouldSucceed() {
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Project project = Project.builder()
                .id(projectId)
                .ownerId(ownerId)
                .status(Project.ProjectStatus.DRAFT)
                .build();

        when(projectRepositoryPort.findById(projectId)).thenReturn(Optional.of(project));
        when(currentUserPort.getCurrentUserId()).thenReturn(ownerId);
        when(taskRepositoryPort.countByProjectId(projectId)).thenReturn(1L);

        activateProjectUseCase.activateProject(projectId);

        verify(projectRepositoryPort).save(project);
        verify(auditLogPort).register("ACTIVATE_PROJECT", projectId);
        verify(notificationPort).notify(anyString());
    }

    @Test
    void ActivateProject_WithoutTasks_ShouldFail() {
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Project project = Project.builder()
                .id(projectId)
                .ownerId(ownerId)
                .status(Project.ProjectStatus.DRAFT)
                .build();

        when(projectRepositoryPort.findById(projectId)).thenReturn(Optional.of(project));
        when(currentUserPort.getCurrentUserId()).thenReturn(ownerId);
        when(taskRepositoryPort.countByProjectId(projectId)).thenReturn(0L);

        assertThrows(IllegalStateException.class, () -> activateProjectUseCase.activateProject(projectId));
        verify(projectRepositoryPort, never()).save(any());
    }

    @Test
    void ActivateProject_ByNonOwner_ShouldFail() {
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        Project project = Project.builder()
                .id(projectId)
                .ownerId(ownerId)
                .status(Project.ProjectStatus.DRAFT)
                .build();

        when(projectRepositoryPort.findById(projectId)).thenReturn(Optional.of(project));
        when(currentUserPort.getCurrentUserId()).thenReturn(otherUserId);

        assertThrows(SecurityException.class, () -> activateProjectUseCase.activateProject(projectId));
        verify(projectRepositoryPort, never()).save(any());
    }
}
