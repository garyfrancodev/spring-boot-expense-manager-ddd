package com.garyfrancodev.ExpenseManagerApplication.use_cases.transaction.command;

import an.awesome.pipelinr.Command;
import com.garyfrancodev.ExpenseManagerApplication.dtos.transaction.UpdateTransactionDto;

import java.util.UUID;

public class UpdateTransactionCommand implements Command<UUID> {
    private final UpdateTransactionDto updateTransactionDto;

    public UpdateTransactionCommand(UpdateTransactionDto updateTransactionDto) {
        this.updateTransactionDto = updateTransactionDto;
    }

    public UpdateTransactionDto getUpdateTransactionDto() {
        return updateTransactionDto;
    }
}
