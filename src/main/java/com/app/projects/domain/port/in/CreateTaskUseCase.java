package com.app.projects.domain.port.in;

import com.app.projects.domain.model.Task;
import java.util.UUID;

public interface CreateTaskUseCase {
    Task createTask(UUID projectId, String title);
}
