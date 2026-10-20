package com.garyfrancodev.ExpenseManagerSharedKernel.valueobjects;

import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.ValueObject;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.NonNegativeAmountRule;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object representing a non-negative counter.
 */
public final class CountValue extends ValueObject {

    private final int value;

    public CountValue(int value) throws BusinessRuleValidationException {
        checkRule(new NonNegativeAmountRule(BigDecimal.valueOf(value), "Count"));
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountValue that = (CountValue) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
