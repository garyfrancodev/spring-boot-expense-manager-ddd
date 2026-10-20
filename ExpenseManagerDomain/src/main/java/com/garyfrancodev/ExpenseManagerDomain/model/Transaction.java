package com.garyfrancodev.ExpenseManagerDomain.model;

import com.garyfrancodev.ExpenseManagerDomain.enums.TransactionType;
import com.garyfrancodev.ExpenseManagerDomain.events.transaction.CreatedTransaction;
import com.garyfrancodev.ExpenseManagerDomain.events.transaction.UpdatedTransaction;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.AggregateRoot;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.GreaterThanZeroRule;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.NotNullRule;
import com.garyfrancodev.ExpenseManagerSharedKernel.valueobjects.TransactionDescription;

import java.util.Date;
import java.util.UUID;

/**
 * Aggregate Root representing an Income or Expense Transaction.
 */
public class Transaction extends AggregateRoot {
    private UUID accountId;
    private UUID userId;
    private UUID categoryId;
    private Double amount;
    private Date created_at;
    private Date date;
    private TransactionDescription transactionDescription;
    private TransactionType transactionType;

    public Transaction(UUID accountId, UUID userId, UUID categoryId, Double amount, Date date, String transactionDescription, TransactionType transactionType) throws BusinessRuleValidationException {
        super(UUID.randomUUID());
        checkRule(new NotNullRule(accountId, "AccountId"));
        checkRule(new NotNullRule(userId, "UserId"));
        checkRule(new NotNullRule(categoryId, "CategoryId"));
        checkRule(new NotNullRule(transactionType, "TransactionType"));
        checkRule(new GreaterThanZeroRule(amount, "Transaction amount"));

        this.accountId = accountId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = Math.round(amount * 100.0) / 100.0;
        this.date = date != null ? date : new Date();
        this.created_at = new Date();
        this.transactionDescription = new TransactionDescription(transactionDescription);
        this.transactionType = transactionType;

        addDomainEvent(new CreatedTransaction(this.id, this.accountId, this.userId, this.amount, this.transactionType, this.date));
    }

    public Transaction(UUID accountId, UUID userId, UUID categoryId, Double amount, Date created_at, Date date, String transactionDescription, TransactionType transactionType, UUID id) throws BusinessRuleValidationException {
        super(id);
        checkRule(new NotNullRule(accountId, "AccountId"));
        checkRule(new NotNullRule(userId, "UserId"));
        checkRule(new NotNullRule(categoryId, "CategoryId"));
        checkRule(new NotNullRule(transactionType, "TransactionType"));
        checkRule(new GreaterThanZeroRule(amount, "Transaction amount"));

        this.accountId = accountId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = Math.round(amount * 100.0) / 100.0;
        this.created_at = created_at != null ? created_at : new Date();
        this.date = date != null ? date : new Date();
        this.transactionDescription = new TransactionDescription(transactionDescription);
        this.transactionType = transactionType;
    }

    public double updateAmount(Double newAmount) throws BusinessRuleValidationException {
        checkRule(new GreaterThanZeroRule(newAmount, "New transaction amount"));
        double rounded = Math.round(newAmount * 100.0) / 100.0;
        double difference = this.amount - rounded;
        this.amount = rounded;

        addDomainEvent(new UpdatedTransaction(this.id, this.accountId, difference, this.transactionType, this.date));
        return difference;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public Double getAmount() {
        return amount;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Date getDate() {
        return date;
    }

    public String getTransactionDescription() {
        return transactionDescription != null ? transactionDescription.getValue() : "";
    }

    public TransactionDescription getTransactionDescriptionValue() {
        return transactionDescription;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }
}
