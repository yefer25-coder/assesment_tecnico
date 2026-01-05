package com.app.projects.application.service;

import com.app.projects.domain.model.User;
import com.app.projects.domain.port.in.LoginUseCase;
import com.app.projects.domain.port.out.PasswordEncoderPort;
import com.app.projects.domain.port.out.TokenProviderPort;
import com.app.projects.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserService implements LoginUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;

    @Override
    public String login(String username, String password) {
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoderPort.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return tokenProviderPort.generateToken(user.getUsername());
    }
}
