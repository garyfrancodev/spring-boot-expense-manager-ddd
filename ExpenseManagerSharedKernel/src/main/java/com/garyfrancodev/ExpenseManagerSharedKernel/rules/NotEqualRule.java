package com.garyfrancodev.ExpenseManagerSharedKernel.rules;

import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRule;

import java.util.Objects;

public class NotEqualRule<T> implements BusinessRule {
    private final T first;
    private final T second;
    private final String description;

    public NotEqualRule(T first, T second, String description) {
        this.first = first;
        this.second = second;
        this.description = description != null ? description : "Values must not be equal";
    }

    @Override
    public boolean isValid() {
        if (first == null && second == null) return false;
        return !Objects.equals(first, second);
    }

    @Override
    public String getMessage() {
        return description;
    }
}
