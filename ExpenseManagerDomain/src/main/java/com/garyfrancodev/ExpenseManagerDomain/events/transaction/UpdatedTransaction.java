package com.garyfrancodev.ExpenseManagerDomain.events.transaction;

import com.garyfrancodev.ExpenseManagerDomain.enums.TransactionType;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.DomainEvent;

import java.util.Date;
import java.util.UUID;

/**
 * Domain Event raised when an existing transaction amount has been updated.
 */
public class UpdatedTransaction extends DomainEvent {
    private final UUID transactionId;
    private final UUID accountId;
    private final Double newAmount;
    private final TransactionType transactionType;

    public UpdatedTransaction(UUID transactionId, UUID accountId, Double newAmount, TransactionType transactionType, Date occurredOn) {
        super(occurredOn);
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.newAmount = newAmount;
        this.transactionType = transactionType;
    }

    public UpdatedTransaction(Date occurredOn, UUID accountId, Double newAmount, TransactionType transactionType) {
        this(null, accountId, newAmount, transactionType, occurredOn);
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public Double getNewAmount() {
        return newAmount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }
}
