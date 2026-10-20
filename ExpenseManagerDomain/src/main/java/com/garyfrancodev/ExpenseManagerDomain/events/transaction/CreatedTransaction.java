package com.garyfrancodev.ExpenseManagerDomain.events.transaction;

import com.garyfrancodev.ExpenseManagerDomain.enums.TransactionType;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.DomainEvent;

import java.util.Date;
import java.util.UUID;

/**
 * Domain Event raised when a new financial transaction has been created.
 */
public class CreatedTransaction extends DomainEvent {
    private final UUID transactionId;
    private final UUID accountId;
    private final UUID userId;
    private final Double amount;
    private final TransactionType transactionType;

    public CreatedTransaction(UUID transactionId, UUID accountId, UUID userId, Double amount, TransactionType transactionType, Date occurredOn) {
        super(occurredOn);
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.userId = userId;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public CreatedTransaction(Date occurredOn, UUID accountId, Double amount, TransactionType transactionType) {
        this(null, accountId, null, amount, transactionType, occurredOn);
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public UUID getUserId() {
        return userId;
    }

    public Double getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }
}
