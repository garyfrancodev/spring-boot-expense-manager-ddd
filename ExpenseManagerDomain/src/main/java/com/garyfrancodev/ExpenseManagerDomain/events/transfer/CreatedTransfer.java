package com.garyfrancodev.ExpenseManagerDomain.events.transfer;

import com.garyfrancodev.ExpenseManagerSharedKernel.core.DomainEvent;

import java.util.Date;
import java.util.UUID;

/**
 * Domain Event raised when an inter-account transfer is created.
 */
public class CreatedTransfer extends DomainEvent {
    private final UUID transferId;
    private final UUID accountSend;
    private final UUID accountReceives;
    private final Double amount;
    private final UUID userId;
    private final String description;
    private final UUID categoryId;

    public CreatedTransfer(UUID transferId, UUID accountSend, UUID accountReceives, Double amount, UUID userId, String description, UUID categoryId, Date occurredOn) {
        super(occurredOn);
        this.transferId = transferId;
        this.accountSend = accountSend;
        this.accountReceives = accountReceives;
        this.amount = amount;
        this.userId = userId;
        this.description = description;
        this.categoryId = categoryId;
    }

    public CreatedTransfer(Date occurredOn, UUID accountSend, UUID accountReceives, Double amount, UUID userId, String description, UUID categoryId) {
        this(null, accountSend, accountReceives, amount, userId, description, categoryId, occurredOn);
    }

    public UUID getTransferId() {
        return transferId;
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

    public UUID getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public UUID getCategoryId() {
        return categoryId;
    }
}
