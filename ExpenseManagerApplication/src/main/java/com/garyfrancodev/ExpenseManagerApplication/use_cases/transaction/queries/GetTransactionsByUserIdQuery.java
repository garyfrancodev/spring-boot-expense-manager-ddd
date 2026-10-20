package com.garyfrancodev.ExpenseManagerApplication.use_cases.transaction.queries;

import an.awesome.pipelinr.Command;
import com.garyfrancodev.ExpenseManagerApplication.dtos.transaction.TransactionDto;

import java.util.List;
import java.util.UUID;

public class GetTransactionsByUserIdQuery implements Command<List<TransactionDto>> {
    private final UUID userId;

    public GetTransactionsByUserIdQuery(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
