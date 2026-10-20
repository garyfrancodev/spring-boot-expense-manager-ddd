package com.garyfrancodev.ExpenseManagerApplication.use_cases.transaction.command;

import an.awesome.pipelinr.Command;
import com.garyfrancodev.ExpenseManagerApplication.dtos.transaction.CreateTransactionDto;

import java.util.UUID;

public class CreateTransactionCommand implements Command<UUID> {
    private final CreateTransactionDto transactionDto;

    public CreateTransactionCommand(CreateTransactionDto transactionDto) {
        this.transactionDto = transactionDto;
    }

    public CreateTransactionDto getTransactionDto() {
        return transactionDto;
    }
}
