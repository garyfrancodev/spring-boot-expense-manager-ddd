package com.garyfrancodev.ExpenseManagerSharedKernel.valueobjects;

import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.ValueObject;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.StringMaxLengthRule;

import java.util.Objects;

/**
 * Value Object representing a transaction note or description.
 *
 * <p><b>Objetivo:</b></p>
 * Permitir descripciones limpias, recortadas y acotadas a 255 caracteres
 * protegiendo los invariantes de almacenamiento y presentación.
 */
public final class TransactionDescription extends ValueObject {

    private static final int MAX_LENGTH = 255;
    private final String value;

    public TransactionDescription(String value) throws BusinessRuleValidationException {
        String trimmed = value != null ? value.trim() : "";
        checkRule(new StringMaxLengthRule(trimmed, MAX_LENGTH, "Transaction description"));
        this.value = trimmed;
    }

    public static TransactionDescription empty() {
        try {
            return new TransactionDescription("");
        } catch (BusinessRuleValidationException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDescription that = (TransactionDescription) o;
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
