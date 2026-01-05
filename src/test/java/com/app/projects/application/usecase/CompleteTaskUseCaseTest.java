package com.app.projects.application.usecase;

import com.app.projects.application.service.CompleteTaskService;
import com.app.projects.domain.model.Project;
import com.app.projects.domain.model.Task;
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
class CompleteTaskUseCaseTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;
    @Mock
    private ProjectRepositoryPort projectRepositoryPort;
    @Mock
    private CurrentUserPort currentUserPort;
    @Mock
    private AuditLogPort auditLogPort;
    @Mock
    private NotificationPort notificationPort;

    @InjectMocks
    private CompleteTaskService completeTaskUseCase;

    @Test
    void CompleteTask_ShouldGenerateAuditAndNotification() {
        UUID taskId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Task task = Task.builder()
                .id(taskId)
                .projectId(projectId)
                .completed(false)
                .build();

        Project project = Project.builder()
                .id(projectId)
                .ownerId(ownerId)
                .build();

        when(taskRepositoryPort.findById(taskId)).thenReturn(Optional.of(task));
        when(projectRepositoryPort.findById(projectId)).thenReturn(Optional.of(project));
        when(currentUserPort.getCurrentUserId()).thenReturn(ownerId);

        completeTaskUseCase.completeTask(taskId);

        verify(taskRepositoryPort).save(task);
        verify(auditLogPort).register("COMPLETE_TASK", taskId);
        verify(notificationPort).notify(anyString());
    }

    @Test
    void CompleteTask_AlreadyCompleted_ShouldFail() {
        UUID taskId = UUID.randomUUID();
        Task task = Task.builder()
                .id(taskId)
                .completed(true)
                .build();

        when(taskRepositoryPort.findById(taskId)).thenReturn(Optional.of(task));

        assertThrows(IllegalStateException.class, () -> completeTaskUseCase.completeTask(taskId));
        verify(taskRepositoryPort, never()).save(any());
    }
}
