package com.garyfrancodev.ExpenseManagerDomain.services;

import com.garyfrancodev.ExpenseManagerDomain.model.User;

public interface AuthService {
    AuthResult login(String email, String password);
    String register(User user);
}
