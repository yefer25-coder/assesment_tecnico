package com.app.projects.infraestructure.security;

import com.app.projects.domain.model.User;
import com.app.projects.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public void registerNewUser(String username, String encryptedPassword, String role) {
        if (userRepositoryPort.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        User newUser = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .password(encryptedPassword)
                .role(role)
                .email(username + "@example.com") // Placeholder email as it's required by domain but not in auth
                                                  // request
                .build();

        userRepositoryPort.save(newUser);
    }

    public boolean validateCredentials(String username, String rawPassword) {
        return userRepositoryPort.findByUsername(username)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }

    public String getUserRole(String username) {
        return userRepositoryPort.findByUsername(username)
                .map(User::getRole)
                .orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().replace("ROLE_", ""))
                .build();
    }
}
