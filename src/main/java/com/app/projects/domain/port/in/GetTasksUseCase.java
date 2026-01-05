package com.app.projects.domain.port.in;

import com.app.projects.domain.model.Task;
import java.util.List;
import java.util.UUID;

public interface GetTasksUseCase {
    List<Task> getTasks(UUID projectId);
}
