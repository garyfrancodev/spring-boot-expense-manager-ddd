package com.garyfrancodev.ExpenseManagerDomain.model;

import com.garyfrancodev.ExpenseManagerDomain.events.transfer.CreatedTransfer;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.AggregateRoot;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.GreaterThanZeroRule;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.NotEqualRule;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.NotNullRule;

import java.util.Date;
import java.util.UUID;

/**
 * Aggregate Root representing an inter-account Transfer.
 */
public class Transfer extends AggregateRoot {
    private final UUID accountSend;
    private final UUID accountReceives;
    private final Double amount;
    private final Date date;
    private final UUID userId;

    public Transfer(UUID accountSend, UUID accountReceives, Double amount, Date date, UUID userId) throws BusinessRuleValidationException {
        super(UUID.randomUUID());
        checkRule(new NotNullRule(accountSend, "Origin account (accountSend)"));
        checkRule(new NotNullRule(accountReceives, "Destination account (accountReceives)"));
        checkRule(new NotEqualRule<>(accountSend, accountReceives, "Origin and destination accounts must be different"));
        checkRule(new NotNullRule(userId, "UserId"));
        checkRule(new GreaterThanZeroRule(amount, "Transfer amount"));

        this.accountSend = accountSend;
        this.accountReceives = accountReceives;
        this.amount = Math.round(amount * 100.0) / 100.0;
        this.date = date != null ? date : new Date();
        this.userId = userId;

        addDomainEvent(new CreatedTransfer(this.id, accountSend, accountReceives, this.amount, userId, "Transfer between accounts", null, this.date));
    }

    public Transfer(UUID accountSend, UUID accountReceives, Double amount, Date date, UUID userId, UUID id) throws BusinessRuleValidationException {
        super(id);
        checkRule(new NotNullRule(accountSend, "Origin account (accountSend)"));
        checkRule(new NotNullRule(accountReceives, "Destination account (accountReceives)"));
        checkRule(new NotEqualRule<>(accountSend, accountReceives, "Origin and destination accounts must be different"));
        checkRule(new NotNullRule(userId, "UserId"));
        checkRule(new GreaterThanZeroRule(amount, "Transfer amount"));

        this.accountSend = accountSend;
        this.accountReceives = accountReceives;
        this.amount = Math.round(amount * 100.0) / 100.0;
        this.userId = userId;
        this.date = date != null ? date : new Date();
    }

    public UUID getAccountSend() {
        return accountSend;
    }

    public UUID getAccountReceives() {
        return accountReceives;
    }

    public Double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public UUID getUserId() {
        return userId;
    }
}
