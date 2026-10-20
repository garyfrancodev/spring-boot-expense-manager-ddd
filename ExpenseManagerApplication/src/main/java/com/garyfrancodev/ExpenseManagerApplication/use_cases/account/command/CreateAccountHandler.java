package com.garyfrancodev.ExpenseManagerApplication.use_cases.account.command;

import an.awesome.pipelinr.Command;
import com.garyfrancodev.ExpenseManagerApplication.dispatcher.DomainEventDispatcher;
import com.garyfrancodev.ExpenseManagerApplication.dtos.account.CreateAccountDto;
import com.garyfrancodev.ExpenseManagerDomain.model.Account;
import com.garyfrancodev.ExpenseManagerDomain.repositories.AccountRepository;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class CreateAccountHandler implements Command.Handler<CreateAccountCommand, UUID> {
    private final AccountRepository _accountRepository;
    private final DomainEventDispatcher _domainEventDispatcher;

    @Autowired
    public CreateAccountHandler(AccountRepository accountRepository, DomainEventDispatcher domainEventDispatcher) {
        this._accountRepository = accountRepository;
        this._domainEventDispatcher = domainEventDispatcher;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UUID handle(CreateAccountCommand createAccountCommand) {
        CreateAccountDto createAccountDto = createAccountCommand.getCreateAccountDto();
        try {
            Account account = new Account(
                    createAccountDto.getAccountName(),
                    createAccountDto.getDescription(),
                    createAccountDto.getAmount(),
                    createAccountDto.getUserId()
            );
            UUID accountId = _accountRepository.create(account);
            _domainEventDispatcher.dispatchEventsFor(account);
            return accountId;
        } catch (BusinessRuleValidationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
