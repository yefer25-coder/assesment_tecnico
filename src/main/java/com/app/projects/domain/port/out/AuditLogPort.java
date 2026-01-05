package com.app.projects.domain.port.out;

import java.util.UUID;

public interface AuditLogPort {
    void register(String action, UUID entityId);
}
