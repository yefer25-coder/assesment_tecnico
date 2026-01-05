package com.app.projects.domain.port.in;

import com.app.projects.domain.model.User;

public interface RegisterUserUseCase {
    User register(String username, String email, String password);
}
