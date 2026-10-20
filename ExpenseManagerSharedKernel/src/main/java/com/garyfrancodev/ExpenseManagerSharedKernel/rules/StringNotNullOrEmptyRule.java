package com.garyfrancodev.ExpenseManagerSharedKernel.rules;

import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRule;

public class StringNotNullOrEmptyRule implements BusinessRule {
    private final String value;
    private final String fieldName;

    public StringNotNullOrEmptyRule(String value) {
        this(value, "String");
    }

    public StringNotNullOrEmptyRule(String value, String fieldName) {
        this.value = value;
        this.fieldName = fieldName != null ? fieldName : "String";
    }

    @Override
    public boolean isValid() {
        return value != null && !value.trim().isEmpty();
    }

    @Override
    public String getMessage() {
        return fieldName + " cannot be null, empty, or only whitespace";
    }
}
