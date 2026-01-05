package com.app.projects.infraestructure.adapter.security;

import com.app.projects.domain.model.User;
import com.app.projects.domain.port.out.CurrentUserPort;
import com.app.projects.domain.port.out.UserRepositoryPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringSecurityCurrentUserAdapter implements CurrentUserPort {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new SecurityException("Unknown principal type");
        }

        return userRepositoryPort.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new SecurityException("User not found: " + username));
    }
}
