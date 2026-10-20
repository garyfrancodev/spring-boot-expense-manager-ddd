package com.garyfrancodev.ExpenseManagerSharedKernel.rules;

import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRule;

import java.util.regex.Pattern;

public class EmailFormatRule implements BusinessRule {
    private final String value;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,63}$");

    public EmailFormatRule(String value) {
        this.value = value;
    }

    @Override
    public boolean isValid() {
        return value != null && !value.trim().isEmpty() && EMAIL_PATTERN.matcher(value.trim()).matches();
    }

    @Override
    public String getMessage() {
        return "Email format is invalid: " + value;
    }
}
