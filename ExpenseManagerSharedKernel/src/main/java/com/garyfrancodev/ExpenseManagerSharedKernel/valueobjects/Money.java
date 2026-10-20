package com.garyfrancodev.ExpenseManagerSharedKernel.valueobjects;

import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.ValueObject;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.NotNullRule;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.StringNotNullOrEmptyRule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object representing a monetary amount with an associated currency.
 *
 * <p><b>Objetivo:</b></p>
 * Eliminar el uso de tipos de coma flotante (Double/Float) propensos a errores
 * de precisión en cálculos contables y financieros, encapsular operaciones monetarias
 * seguras y garantizar inmutabilidad.
 */
public final class Money extends ValueObject implements Comparable<Money> {

    public static final String DEFAULT_CURRENCY = "USD";
    private static final int DEFAULT_SCALE = 2;
    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;

    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) throws BusinessRuleValidationException {
        checkRule(new NotNullRule(amount, "Amount"));
        checkRule(new StringNotNullOrEmptyRule(currency, "Currency"));
        this.amount = amount.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING);
        this.currency = currency.trim().toUpperCase();
    }

    public Money(BigDecimal amount) throws BusinessRuleValidationException {
        this(amount, DEFAULT_CURRENCY);
    }

    public Money(Double amount) throws BusinessRuleValidationException {
        this(amount != null ? BigDecimal.valueOf(amount) : null, DEFAULT_CURRENCY);
    }

    public Money(Double amount, String currency) throws BusinessRuleValidationException {
        this(amount != null ? BigDecimal.valueOf(amount) : null, currency);
    }

    public static Money zero(String currency) {
        try {
            return new Money(BigDecimal.ZERO, currency);
        } catch (BusinessRuleValidationException e) {
            throw new IllegalStateException("Unexpected error creating zero money", e);
        }
    }

    public static Money zero() {
        return zero(DEFAULT_CURRENCY);
    }

    public static Money of(double amount) throws BusinessRuleValidationException {
        return new Money(amount);
    }

    public static Money of(BigDecimal amount) throws BusinessRuleValidationException {
        return new Money(amount);
    }

    public Money add(Money other) throws BusinessRuleValidationException {
        checkRule(new NotNullRule(other, "Other Money"));
        assertSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) throws BusinessRuleValidationException {
        checkRule(new NotNullRule(other, "Other Money"));
        assertSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    public Money multiply(double factor) throws BusinessRuleValidationException {
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)), this.currency);
    }

    public boolean isPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isNegative() {
        return this.amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isGreaterThan(Money other) {
        assertSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isGreaterThanOrEqual(Money other) {
        assertSameCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
    }

    public boolean isLessThan(Money other) {
        assertSameCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    private void assertSameCurrency(Money other) {
        if (!this.currency.equalsIgnoreCase(other.currency)) {
            throw new IllegalArgumentException("Cannot operate with different currencies: " + this.currency + " and " + other.currency);
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public double toDouble() {
        return amount.doubleValue();
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public int compareTo(Money o) {
        assertSameCurrency(o);
        return this.amount.compareTo(o.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0 && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros(), currency);
    }

    @Override
    public String toString() {
        return currency + " " + amount.toPlainString();
    }
}
