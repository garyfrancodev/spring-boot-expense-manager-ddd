package com.garyfrancodev.ExpenseManagerApplication.use_cases.transaction.command;

import an.awesome.pipelinr.Command;
import com.garyfrancodev.ExpenseManagerApplication.dispatcher.DomainEventDispatcher;
import com.garyfrancodev.ExpenseManagerApplication.dtos.transaction.UpdateTransactionDto;
import com.garyfrancodev.ExpenseManagerDomain.model.Transaction;
import com.garyfrancodev.ExpenseManagerDomain.repositories.TransactionRepository;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
public class UpdateTransactionHandler implements Command.Handler<UpdateTransactionCommand, UUID> {

    private final TransactionRepository _transactionRepository;
    private final DomainEventDispatcher _domainEventDispatcher;

    @Autowired
    public UpdateTransactionHandler(TransactionRepository transactionRepository, DomainEventDispatcher domainEventDispatcher) {
        this._transactionRepository = transactionRepository;
        this._domainEventDispatcher = domainEventDispatcher;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UUID handle(UpdateTransactionCommand updateTransactionCommand) {
        UpdateTransactionDto updateTransactionDto = updateTransactionCommand.getUpdateTransactionDto();
        Transaction transaction = this._transactionRepository.get(updateTransactionDto.getId());
        if (transaction == null) {
            throw new NoSuchElementException("Transaction not found with ID: " + updateTransactionDto.getId());
        }

        try {
            transaction.updateAmount(updateTransactionDto.getAmount());
            this._transactionRepository.update(transaction);
            this._domainEventDispatcher.dispatchEventsFor(transaction);
            return transaction.getId();
        } catch (BusinessRuleValidationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
