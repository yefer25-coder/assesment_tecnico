package com.app.projects.infraestructure.adapter.audit;

import com.app.projects.domain.port.out.AuditLogPort;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsoleAuditLogAdapter implements AuditLogPort {

    @Override
    public void register(String action, UUID entityId) {
        log.info("AUDIT: Action={} EntityId={}", action, entityId);
    }
}
