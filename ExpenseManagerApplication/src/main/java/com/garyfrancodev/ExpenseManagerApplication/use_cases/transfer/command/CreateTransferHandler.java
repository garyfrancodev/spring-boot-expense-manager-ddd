package com.garyfrancodev.ExpenseManagerApplication.use_cases.transfer.command;

import an.awesome.pipelinr.Command;
import com.garyfrancodev.ExpenseManagerApplication.dispatcher.DomainEventDispatcher;
import com.garyfrancodev.ExpenseManagerApplication.dtos.transfer.CreateTransferDto;
import com.garyfrancodev.ExpenseManagerDomain.model.Transfer;
import com.garyfrancodev.ExpenseManagerDomain.repositories.TransferRepository;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class CreateTransferHandler implements Command.Handler<CreateTransferCommand, UUID> {

    private final TransferRepository _transferRepository;
    private final DomainEventDispatcher _domainEventDispatcher;

    @Autowired
    public CreateTransferHandler(TransferRepository transferRepository, DomainEventDispatcher domainEventDispatcher) {
        this._transferRepository = transferRepository;
        this._domainEventDispatcher = domainEventDispatcher;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UUID handle(CreateTransferCommand createTransferCommand) {
        CreateTransferDto createTransferDto = createTransferCommand.getCreateTransferDto();
        try {
            Transfer transfer = new Transfer(
                    createTransferDto.getAccountSend(),
                    createTransferDto.getAccountReceives(),
                    createTransferDto.getAmount(),
                    createTransferDto.getDate(),
                    createTransferDto.getUserId()
            );

            UUID id = _transferRepository.create(transfer);
            _domainEventDispatcher.dispatchEventsFor(transfer);
            return id;
        } catch (BusinessRuleValidationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
