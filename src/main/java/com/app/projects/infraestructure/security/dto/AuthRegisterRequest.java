package com.creditapplicationservice.coopcredit.infraestructure.rest.dto;

import lombok.Data;

@Data
public class AuthRegisterRequest {
    private String username;
    private String password;
    private String role;
}
