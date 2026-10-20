package com.garyfrancodev.ExpenseManagerApplication.use_cases.user;

import an.awesome.pipelinr.Command;
import com.garyfrancodev.ExpenseManagerApplication.dtos.user.LoginDto;
import com.garyfrancodev.ExpenseManagerDomain.services.AuthResult;
import com.garyfrancodev.ExpenseManagerDomain.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginUserHandler implements Command.Handler<LoginUserCommand, AuthResult> {
    private final AuthService authService;

    @Autowired
    public LoginUserHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public AuthResult handle(LoginUserCommand loginUserCommand) {
        LoginDto loginDto = loginUserCommand.getLoginDto();
        return authService.login(loginDto.getEmail(), loginDto.getPassword());
    }
}
