package com.garyfrancodev.ExpenseManagerSharedKernel.valueobjects;

import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.ValueObject;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.StringMaxLengthRule;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.StringNotNullOrEmptyRule;

import java.util.Objects;

/**
 * Value Object representing a validated account name.
 *
 * <p><b>Objetivo:</b></p>
 * Representar un nombre descriptivo de cuenta financiera (ej. "Checking Account", "BCP Ahorros")
 * impidiendo nombres en blanco o excesivamente largos.
 */
public final class AccountName extends ValueObject {

    private static final int MAX_LENGTH = 60;
    private final String value;

    public AccountName(String value) throws BusinessRuleValidationException {
        checkRule(new StringNotNullOrEmptyRule(value, "Account name"));
        checkRule(new StringMaxLengthRule(value != null ? value.trim() : null, MAX_LENGTH, "Account name"));
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountName that = (AccountName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
