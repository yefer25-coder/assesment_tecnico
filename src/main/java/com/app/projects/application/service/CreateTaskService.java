package com.app.projects.application.service;

import com.app.projects.domain.model.Task;
import com.app.projects.domain.port.in.CreateTaskUseCase;
import com.app.projects.domain.port.out.TaskRepositoryPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTaskService implements CreateTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public Task createTask(UUID projectId, String title) {
        Task task = Task.builder()
                .id(UUID.randomUUID())
                .projectId(projectId)
                .title(title)
                .completed(false)
                .deleted(false)
                .build();
        return taskRepositoryPort.save(task);
    }
}
