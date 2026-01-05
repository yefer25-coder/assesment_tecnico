package com.app.projects.domain.port.out;

import com.app.projects.domain.model.User;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);

    Optional<User> findByUsername(String username);
}
