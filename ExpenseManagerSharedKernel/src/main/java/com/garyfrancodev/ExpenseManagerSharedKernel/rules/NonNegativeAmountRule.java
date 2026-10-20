package com.garyfrancodev.ExpenseManagerSharedKernel.rules;

import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRule;

import java.math.BigDecimal;

public class NonNegativeAmountRule implements BusinessRule {
    private final BigDecimal value;
    private final String fieldName;

    public NonNegativeAmountRule(BigDecimal value, String fieldName) {
        this.value = value;
        this.fieldName = fieldName != null ? fieldName : "Amount";
    }

    public NonNegativeAmountRule(Double value, String fieldName) {
        this(value != null ? BigDecimal.valueOf(value) : null, fieldName);
    }

    @Override
    public boolean isValid() {
        return value != null && value.compareTo(BigDecimal.ZERO) >= 0;
    }

    @Override
    public String getMessage() {
        return fieldName + " cannot be negative";
    }
}
