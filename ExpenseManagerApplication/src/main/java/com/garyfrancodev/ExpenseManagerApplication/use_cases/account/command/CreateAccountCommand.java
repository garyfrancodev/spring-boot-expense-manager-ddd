package com.garyfrancodev.ExpenseManagerApplication.use_cases.account.command;

import an.awesome.pipelinr.Command;
import com.garyfrancodev.ExpenseManagerApplication.dtos.account.CreateAccountDto;

import java.util.UUID;

public class CreateAccountCommand implements Command<UUID> {
    private final CreateAccountDto createAccountDto;

    public CreateAccountCommand(CreateAccountDto createAccountDto) {
        this.createAccountDto = createAccountDto;
    }

    public CreateAccountDto getCreateAccountDto() {
        return createAccountDto;
    }
}
