package com.app.projects.infraestructure.security.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}

