package com.app.projects.domain.port.in;

import java.util.UUID;

public interface ActivateProjectUseCase {
    void activateProject(UUID projectId);
}
