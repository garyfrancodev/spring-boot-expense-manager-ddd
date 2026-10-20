package com.garyfrancodev.ExpenseManagerSharedKernel.valueobjects;

import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.ValueObject;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.StringMaxLengthRule;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.StringNotNullOrEmptyRule;

import java.util.Objects;

/**
 * Value Object representing a validated category name.
 *
 * <p><b>Objetivo:</b></p>
 * Asegurar que el nombre de una categoría no sea nulo, vacío ni exceda
 * el límite de longitud del dominio (50 caracteres).
 */
public final class CategoryNameValue extends ValueObject {

    private static final int MAX_LENGTH = 50;
    private final String name;

    public CategoryNameValue(String name) throws BusinessRuleValidationException {
        checkRule(new StringNotNullOrEmptyRule(name, "Category name"));
        checkRule(new StringMaxLengthRule(name != null ? name.trim() : null, MAX_LENGTH, "Category name"));
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryNameValue that = (CategoryNameValue) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
