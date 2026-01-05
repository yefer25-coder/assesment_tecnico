package com.app.projects.infraestructure.security.dto;

import lombok.Data;

@Data
public class AuthRegisterRequest {
    private String username;
    private String email;
    private String password;
}
