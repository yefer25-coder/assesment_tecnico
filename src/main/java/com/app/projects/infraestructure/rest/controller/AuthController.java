package com.app.projects.infraestructure.rest.controller;

import com.app.projects.domain.port.in.LoginUseCase;
import com.app.projects.domain.port.in.RegisterUserUseCase;
import com.app.projects.infraestructure.security.dto.AuthRegisterRequest;
import com.app.projects.infraestructure.security.dto.AuthRequest;
import com.app.projects.infraestructure.security.dto.AuthResponse;
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
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRegisterRequest request) {
        registerUserUseCase.register(request.getUsername(), request.getEmail(), request.getPassword());
        // Auto-login or just return success message?
        // Requirement says "registro de un usuario".
        // Usually return token or just 201.
        // Let's return a success message for now, or generate token if we want
        // auto-login.
        // The original AuthService returned a token.
        // But RegisterUserUseCase returns User.
        // If we want token, we need to generate it.
        // But LoginUseCase generates token.
        // Let's just return success message for now to keep it simple, or call login.
        // Let's return "User registered successfully".
        return ResponseEntity.ok(new AuthResponse(null, "Usuario registrado correctamente"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        String token = loginUseCase.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token, "Login exitoso"));
    }
}
