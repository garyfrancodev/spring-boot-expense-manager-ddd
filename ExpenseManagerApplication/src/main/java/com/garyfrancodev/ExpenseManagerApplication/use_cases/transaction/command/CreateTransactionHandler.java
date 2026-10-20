package com.garyfrancodev.ExpenseManagerApplication.use_cases.transaction.command;

import an.awesome.pipelinr.Command;
import com.garyfrancodev.ExpenseManagerApplication.dispatcher.DomainEventDispatcher;
import com.garyfrancodev.ExpenseManagerApplication.dtos.transaction.CreateTransactionDto;
import com.garyfrancodev.ExpenseManagerDomain.model.Transaction;
import com.garyfrancodev.ExpenseManagerDomain.repositories.TransactionRepository;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class CreateTransactionHandler implements Command.Handler<CreateTransactionCommand, UUID> {

    private final TransactionRepository _transactionRepository;
    private final DomainEventDispatcher _domainEventDispatcher;

    @Autowired
    public CreateTransactionHandler(TransactionRepository transactionRepository, DomainEventDispatcher domainEventDispatcher) {
        this._transactionRepository = transactionRepository;
        this._domainEventDispatcher = domainEventDispatcher;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UUID handle(CreateTransactionCommand createTransactionCommand) {
        CreateTransactionDto transactionDto = createTransactionCommand.getTransactionDto();
        try {
            Transaction transaction = new Transaction(
                    transactionDto.getAccountId(),
                    transactionDto.getUserId(),
                    transactionDto.getCategoryId(),
                    transactionDto.getAmount(),
                    transactionDto.getDate(),
                    transactionDto.getTransactionDescription(),
                    transactionDto.getTransactionType()
            );

            UUID id = _transactionRepository.create(transaction);
            _domainEventDispatcher.dispatchEventsFor(transaction);
            return id;
        } catch (BusinessRuleValidationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
