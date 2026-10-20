package com.garyfrancodev.ExpenseManagerApplication.dtos.transaction;

import com.garyfrancodev.ExpenseManagerDomain.enums.TransactionType;

import java.util.Date;
import java.util.UUID;

public class CreateTransactionDto {
    private UUID accountId;
    private UUID userId;
    private UUID categoryId;
    private double amount;
    private Date date;
    private String transactionDescription;
    private TransactionType transactionType;
    private UUID id;

    public CreateTransactionDto() {}

    public CreateTransactionDto(UUID accountId, UUID userId, UUID categoryId, double amount, Date date, String transactionDescription, TransactionType transactionType) {
        this.accountId = accountId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.transactionDescription = transactionDescription;
        this.transactionType = transactionType;
    }

    public UUID getAccountId() { return accountId; }
    public void setAccountId(UUID accountId) { this.accountId = accountId; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public UUID getCategoryId() { return categoryId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }
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
