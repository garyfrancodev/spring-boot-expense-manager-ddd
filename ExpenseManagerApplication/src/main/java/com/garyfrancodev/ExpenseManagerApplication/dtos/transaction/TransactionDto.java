package com.garyfrancodev.ExpenseManagerApplication.dtos.transaction;

import com.garyfrancodev.ExpenseManagerDomain.enums.TransactionType;

import java.util.Date;
import java.util.UUID;

public class TransactionDto {
    private UUID accountId;
    private UUID userId;
    private double amount;
    private Date date;
    private String transactionDescription;
    private TransactionType transactionType;
    private UUID id;

    public TransactionDto() {}

    public TransactionDto(UUID accountId, UUID userId, double amount, Date date, String transactionDescription, TransactionType transactionType, UUID id) {
        this.accountId = accountId;
        this.userId = userId;
        this.amount = amount;
        this.date = date;
        this.transactionDescription = transactionDescription;
        this.transactionType = transactionType;
        this.id = id;
    }

    public UUID getAccountId() { return accountId; }
    public void setAccountId(UUID accountId) { this.accountId = accountId; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getTransactionDescription() { return transactionDescription; }
    public void setTransactionDescription(String transactionDescription) { this.transactionDescription = transactionDescription; }
    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
}
