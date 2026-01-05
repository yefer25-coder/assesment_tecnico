package com.app.projects.infraestructure.adapter.persistence;

import com.app.projects.domain.model.User;
import com.app.projects.domain.port.out.UserRepositoryPort;
import com.app.projects.infraestructure.adapter.persistence.entity.UserEntity;
import com.app.projects.infraestructure.adapter.persistence.repository.UserJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        UserEntity entity = UserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
        UserEntity savedEntity = userJpaRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(this::mapToDomain);
    }

    private User mapToDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRole())
                .build();
    }
}
