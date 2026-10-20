package com.garyfrancodev.ExpenseManagerSharedKernel.valueobjects;

import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRule;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.ValueObject;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.NotNullRule;

import java.util.Date;
import java.util.Objects;

/**
 * Value Object representing a closed time interval [startDate, endDate].
 *
 * <p><b>Objetivo:</b></p>
 * Encapsular consultas temporales (filtrado por período) garantizando que
 * la fecha de inicio sea menor o igual a la fecha de término.
 */
public final class DateRange extends ValueObject {

    private final Date startDate;
    private final Date endDate;

    public DateRange(Date startDate, Date endDate) throws BusinessRuleValidationException {
        checkRule(new NotNullRule(startDate, "Start date"));
        checkRule(new NotNullRule(endDate, "End date"));
        checkRule(new BusinessRule() {
            @Override
            public boolean isValid() {
                return !startDate.after(endDate);
            }

            @Override
            public String getMessage() {
                return "Start date must be before or equal to end date";
            }
        });

        this.startDate = new Date(startDate.getTime());
        this.endDate = new Date(endDate.getTime());
    }

    public boolean includes(Date date) {
        if (date == null) return false;
        return (date.equals(startDate) || date.after(startDate)) &&
               (date.equals(endDate) || date.before(endDate));
    }

    public Date getStartDate() {
        return new Date(startDate.getTime());
    }

    public Date getEndDate() {
        return new Date(endDate.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateRange dateRange = (DateRange) o;
        return Objects.equals(startDate, dateRange.startDate) &&
               Objects.equals(endDate, dateRange.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }

    @Override
    public String toString() {
        return "[" + startDate + " to " + endDate + "]";
    }
}
