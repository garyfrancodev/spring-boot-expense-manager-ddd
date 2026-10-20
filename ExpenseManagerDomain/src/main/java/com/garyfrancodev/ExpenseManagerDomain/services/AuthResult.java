package com.garyfrancodev.ExpenseManagerDomain.services;

import java.io.Serializable;
import java.util.UUID;

public class AuthResult implements Serializable {
    private final String token;
    private final UUID userId;
    private final String email;

    public AuthResult(String token, UUID userId, String email) {
        this.token = token;
        this.userId = userId;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}
