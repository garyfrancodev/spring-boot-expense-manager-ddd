package com.garyfrancodev.ExpenseManagerApplication.dtos.account;

import java.util.UUID;

public class CreateAccountDto {
    public String accountName;
    public String description;
    public double amount;
    private UUID userId;

    public CreateAccountDto() {}

    public CreateAccountDto(String accountName, String description, double amount, UUID userId) {
        this.accountName = accountName;
        this.description = description;
        this.amount = amount;
        this.userId = userId;
    }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
}
