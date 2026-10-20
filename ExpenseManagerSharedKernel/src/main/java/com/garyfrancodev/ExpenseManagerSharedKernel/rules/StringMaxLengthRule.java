package com.garyfrancodev.ExpenseManagerSharedKernel.rules;

import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRule;

public class StringMaxLengthRule implements BusinessRule {
    private final String value;
    private final int maxLength;
    private final String fieldName;

    public StringMaxLengthRule(String value, int maxLength, String fieldName) {
        this.value = value;
        this.maxLength = maxLength;
        this.fieldName = fieldName != null ? fieldName : "Text";
    }

    @Override
    public boolean isValid() {
        return value == null || value.length() <= maxLength;
    }

    @Override
    public String getMessage() {
        return fieldName + " exceeds maximum allowed length of " + maxLength + " characters";
    }
}
