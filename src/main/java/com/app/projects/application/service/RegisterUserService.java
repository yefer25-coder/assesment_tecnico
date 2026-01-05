package com.app.projects.application.service;

import com.app.projects.domain.model.User;
import com.app.projects.domain.port.in.RegisterUserUseCase;
import com.app.projects.domain.port.out.PasswordEncoderPort;
import com.app.projects.domain.port.out.UserRepositoryPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    public User register(String username, String email, String password) {
        if (userRepositoryPort.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check if email exists? UserRepositoryPort might need findByEmail.
        // For now, assuming username is unique identifier for login.

        String encodedPassword = passwordEncoderPort.encode(password);

        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .email(email)
                .password(encodedPassword)
                .build();

        return userRepositoryPort.save(user);
    }
}
