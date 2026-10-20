package com.garyfrancodev.ExpenseManagerApplication.use_cases.transfer.command;

import an.awesome.pipelinr.Command;
import com.garyfrancodev.ExpenseManagerApplication.dtos.transfer.CreateTransferDto;

import java.util.UUID;

public class CreateTransferCommand implements Command<UUID> {
    private final CreateTransferDto createTransferDto;

    public CreateTransferCommand(CreateTransferDto createTransferDto) {
        this.createTransferDto = createTransferDto;
    }

    public CreateTransferDto getCreateTransferDto() {
        return createTransferDto;
    }
}
