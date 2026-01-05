package com.creditapplicationservice.coopcredit.infraestructure.security;

import com.creditapplicationservice.coopcredit.infraestructure.persistence.entity.UserEntity;
import com.creditapplicationservice.coopcredit.infraestructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerNewUser(String username, String encryptedPassword, String role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        UserEntity newUser = UserEntity.builder()
                .username(username)
                .password(encryptedPassword) // Ya debe venir encriptada desde el AuthService
                .role(role)
                .build();

        userRepository.save(newUser);
    }

    public boolean validateCredentials(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }

    public String getUserRole(String username) {
        return userRepository.findByUsername(username)
                .map(UserEntity::getRole)
                .orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().replace("ROLE_", "")) // Spring espera el rol sin el prefijo en este builder
                .build();
    }
}
