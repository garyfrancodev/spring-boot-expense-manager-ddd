package com.garyfrancodev.ExpenseManagerApplication.use_cases.user;

import an.awesome.pipelinr.Command;
import com.garyfrancodev.ExpenseManagerApplication.dispatcher.DomainEventDispatcher;
import com.garyfrancodev.ExpenseManagerApplication.dtos.user.CreateUserDto;
import com.garyfrancodev.ExpenseManagerDomain.model.User;
import com.garyfrancodev.ExpenseManagerDomain.services.AuthService;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class CreateUserHandler implements Command.Handler<CreateUserCommand, UUID> {
    private final AuthService _authService;
    private final DomainEventDispatcher _domainEventDispatcher;

    @Autowired
    public CreateUserHandler(AuthService authService, DomainEventDispatcher domainEventDispatcher) {
        this._authService = authService;
        this._domainEventDispatcher = domainEventDispatcher;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UUID handle(CreateUserCommand createUserCommand) {
        CreateUserDto createUserDto = createUserCommand.getCreateUserDto();
        try {
            User user = new User(
                    createUserDto.getFirstName(),
                    createUserDto.getLastName(),
                    createUserDto.getEmail(),
                    createUserDto.getPassword()
            );

            _authService.register(user);
            _domainEventDispatcher.dispatchEventsFor(user);
            return user.getId();
        } catch (BusinessRuleValidationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
