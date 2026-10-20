package com.garyfrancodev.ExpenseManagerSharedKernel.valueobjects;

import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.ValueObject;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.EmailFormatRule;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.NotNullRule;

import java.util.Locale;
import java.util.Objects;

/**
 * Value Object representing a validated user email address.
 *
 * <p><b>Objetivo:</b></p>
 * Garantizar que todo email en el dominio posea formato RFC válido y
 * normalización canónica en minúsculas y sin espacios.
 */
public final class UserEmailValue extends ValueObject {

    private final String value;

    public UserEmailValue(String value) throws BusinessRuleValidationException {
        checkRule(new NotNullRule(value, "Email"));
        checkRule(new EmailFormatRule(value));
        this.value = value.trim().toLowerCase(Locale.ROOT);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEmailValue that = (UserEmailValue) o;
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
