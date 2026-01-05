package com.creditapplicationservice.coopcredit.infraestructure.security;

import com.creditapplicationservice.coopcredit.infraestructure.rest.dto.AuthRegisterRequest;
import com.creditapplicationservice.coopcredit.infraestructure.rest.dto.AuthRequest;
import com.creditapplicationservice.coopcredit.infraestructure.rest.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AuthService(UserDetailsServiceImpl userDetailsService,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(AuthRegisterRequest request) {
        userDetailsService.registerNewUser(request.getUsername(),
                                           passwordEncoder.encode(request.getPassword()),
                                           request.getRole());
        String token = jwtService.generateToken(request.getUsername(), request.getRole());
        return new AuthResponse(token, "Usuario registrado correctamente");
    }

    public AuthResponse login(AuthRequest request) {
        boolean valid = userDetailsService.validateCredentials(request.getUsername(), request.getPassword());
        if (!valid) {
            return new AuthResponse(null, "Credenciales inválidas");
        }
        String role = userDetailsService.getUserRole(request.getUsername());
        String token = jwtService.generateToken(request.getUsername(), role);
        return new AuthResponse(token, "Login exitoso");
    }
}
