package com.garyfrancodev.ExpenseManagerSharedKernel.rules;

import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRule;

public class NotNullRule implements BusinessRule {
    private final Object value;
    private final String fieldName;

    public NotNullRule(Object value) {
        this(value, "Value");
    }

    public NotNullRule(Object value, String fieldName) {
        this.value = value;
        this.fieldName = fieldName != null ? fieldName : "Value";
    }

    @Override
    public boolean isValid() {
        return value != null;
    }

    @Override
    public String getMessage() {
        return fieldName + " cannot be null";
    }
}
