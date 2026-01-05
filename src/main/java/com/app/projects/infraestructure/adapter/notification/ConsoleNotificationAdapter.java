package com.app.projects.infraestructure.adapter.notification;

import com.app.projects.domain.port.out.NotificationPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsoleNotificationAdapter implements NotificationPort {

    @Override
    public void notify(String message) {
        log.info("NOTIFICATION: {}", message);
    }
}
