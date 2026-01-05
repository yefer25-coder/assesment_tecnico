package com.app.projects.infraestructure.adapter.security;

import com.app.projects.domain.port.out.TokenProviderPort;
import com.app.projects.infraestructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProviderAdapter implements TokenProviderPort {

    private final JwtService jwtService;

    @Override
    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    @Override
    public boolean validateToken(String token, String username) {
        return jwtService.validateToken(token, username);
    }

    @Override
    public String extractUsername(String token) {
        return jwtService.extractUsername(token);
    }
}
