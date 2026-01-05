package com.app.projects.infraestructure.rest.controller;

import com.app.projects.domain.port.in.LoginUseCase;
import com.app.projects.domain.port.in.RegisterUserUseCase;
import com.app.projects.infraestructure.security.dto.AuthRegisterRequest;
import com.app.projects.infraestructure.security.dto.AuthRequest;
import com.app.projects.infraestructure.security.dto.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRegisterRequest request) {
        registerUserUseCase.register(request.getUsername(), request.getEmail(), request.getPassword());
        // For simplicity, we'll just return a token after registration.
        String token = loginUseCase.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        String token = loginUseCase.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
