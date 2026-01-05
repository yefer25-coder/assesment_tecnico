package com.app.projects.domain.port.out;

public interface TokenProviderPort {
    String generateToken(String username);

    boolean validateToken(String token, String username);

    String extractUsername(String token);
}
