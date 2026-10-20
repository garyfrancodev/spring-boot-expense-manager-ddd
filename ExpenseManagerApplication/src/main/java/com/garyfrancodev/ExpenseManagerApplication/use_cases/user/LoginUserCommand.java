package com.garyfrancodev.ExpenseManagerApplication.use_cases.user;

import an.awesome.pipelinr.Command;
import com.garyfrancodev.ExpenseManagerApplication.dtos.user.LoginDto;
import com.garyfrancodev.ExpenseManagerDomain.services.AuthResult;

public class LoginUserCommand implements Command<AuthResult> {
    private final LoginDto loginDto;

    public LoginUserCommand(LoginDto loginDto) {
        this.loginDto = loginDto;
    }

    public LoginDto getLoginDto() {
        return loginDto;
    }
}
