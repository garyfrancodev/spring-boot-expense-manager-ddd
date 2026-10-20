package com.garyfrancodev.ExpenseManagerDomain.model;

import com.garyfrancodev.ExpenseManagerDomain.enums.TransactionType;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.AggregateRoot;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.GreaterThanZeroRule;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.NonNegativeAmountRule;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.NotNullRule;
import com.garyfrancodev.ExpenseManagerSharedKernel.valueobjects.AccountName;

import java.util.UUID;

/**
 * Aggregate Root representing a financial Account.
 *
 * <p><b>Invariantes protegidos:</b></p>
 * <ul>
 *   <li>Nombre de cuenta no vacío y válido.</li>
 *   <li>Pertenece a un usuario identificado.</li>
 *   <li>Saldo inicial no negativo.</li>
 *   <li>Depósitos y retiros deben ser estrictamente positivos.</li>
 *   <li>Reglas contables según el tipo de transacción encapsuladas en el agregado.</li>
 * </ul>
 */
public class Account extends AggregateRoot {
    private AccountName accountName;
    private String description;
    private double amount;
    private UUID userId;

    public Account(double amount) {
        super(UUID.randomUUID());
        this.amount = amount;
    }

    public Account(String accountName, String description, double amount, UUID userId) throws BusinessRuleValidationException {
        super(UUID.randomUUID());
        checkRule(new NotNullRule(userId, "UserId"));
        checkRule(new NonNegativeAmountRule(amount, "Initial balance"));

        this.accountName = new AccountName(accountName);
        this.description = description != null ? description.trim() : "";
        this.amount = Math.round(amount * 100.0) / 100.0;
        this.userId = userId;
    }

    public Account(String accountName, String description, double amount, UUID userId, UUID id) throws BusinessRuleValidationException {
        super(id);
        checkRule(new NotNullRule(userId, "UserId"));
        checkRule(new NonNegativeAmountRule(amount, "Initial balance"));

        this.accountName = new AccountName(accountName);
        this.description = description != null ? description.trim() : "";
        this.amount = Math.round(amount * 100.0) / 100.0;
        this.userId = userId;
    }

    /**
     * Ingresar dinero a la cuenta.
     */
    public void deposit(Double depositAmount) throws BusinessRuleValidationException {
        checkRule(new GreaterThanZeroRule(depositAmount, "Deposit amount"));
        this.amount = Math.round((this.amount + depositAmount) * 100.0) / 100.0;
    }

    /**
     * Retirar dinero de la cuenta protegiendo el saldo.
     */
    public void withdraw(Double withdrawAmount) throws BusinessRuleValidationException {
        checkRule(new GreaterThanZeroRule(withdrawAmount, "Withdraw amount"));
        if (withdrawAmount > this.amount) {
            throw new BusinessRuleValidationException("Insufficient funds in account: " + getAccountName() + ". Current balance: " + this.amount + ", requested: " + withdrawAmount);
        }
        this.amount = Math.round((this.amount - withdrawAmount) * 100.0) / 100.0;
    }

    /**
     * Aplica el impacto financiero de una transacción directamente en el saldo de la cuenta.
     */
    public void applyTransaction(TransactionType transactionType, Double transactionAmount) throws BusinessRuleValidationException {
        checkRule(new NotNullRule(transactionType, "TransactionType"));
        checkRule(new GreaterThanZeroRule(transactionAmount, "Transaction amount"));

        if (transactionType == TransactionType.INCOME || transactionType == TransactionType.INCOME_TRANSFER) {
            deposit(transactionAmount);
        } else if (transactionType == TransactionType.EXPENSE || transactionType == TransactionType.EXPENSE_TRANSFER) {
            withdraw(transactionAmount);
        }
    }

    /**
     * Ajusta el saldo cuando una transacción previa cambia de monto (diferencia).
     */
    public void applyTransactionDifference(TransactionType transactionType, Double difference) throws BusinessRuleValidationException {
        checkRule(new NotNullRule(transactionType, "TransactionType"));
        if (difference == null || difference == 0.0) {
            return;
        }

        if (transactionType == TransactionType.INCOME || transactionType == TransactionType.INCOME_TRANSFER) {
            if (difference > 0) {
                // Nuevo monto fue menor que el anterior (monto anterior - nuevo > 0)
                withdraw(difference);
            } else {
                deposit(Math.abs(difference));
            }
        } else if (transactionType == TransactionType.EXPENSE || transactionType == TransactionType.EXPENSE_TRANSFER) {
            if (difference > 0) {
                // Gasto anterior fue mayor, por ende se gastó menos: devolver saldo
                deposit(difference);
            } else {
                // Gasto nuevo fue mayor: debitar diferencia
                withdraw(Math.abs(difference));
            }
        }
    }

    public void updateAmount(Double updateAmount) throws BusinessRuleValidationException {
        checkRule(new NonNegativeAmountRule(updateAmount, "Balance"));
        this.amount = Math.round(updateAmount * 100.0) / 100.0;
    }

    public void increaseAmount(Double updateAmount) {
        try {
            deposit(updateAmount);
        } catch (BusinessRuleValidationException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public void decreaseAmount(Double updateAmount) {
        try {
            withdraw(updateAmount);
        } catch (BusinessRuleValidationException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public String getAccountName() {
        return accountName != null ? accountName.getValue() : "";
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public UUID getUserId() {
        return userId;
    }
}
