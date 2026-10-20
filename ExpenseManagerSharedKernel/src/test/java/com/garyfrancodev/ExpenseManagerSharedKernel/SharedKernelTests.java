package com.garyfrancodev.ExpenseManagerSharedKernel;

import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.*;
import com.garyfrancodev.ExpenseManagerSharedKernel.valueobjects.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SharedKernelTests {

    @Test
    void testMoneyValidationsAndMath() throws BusinessRuleValidationException {
        Money m1 = Money.of(100.50);
        Money m2 = Money.of(49.50);

        Money sum = m1.add(m2);
        assertEquals(150.00, sum.toDouble());
        assertTrue(sum.isPositive());

        Money diff = m1.subtract(m2);
        assertEquals(51.00, diff.toDouble());

        Money zero = Money.zero("USD");
        assertTrue(zero.isZero());

        assertThrows(BusinessRuleValidationException.class, () -> new Money((BigDecimal) null));
    }

    @Test
    void testUserEmailValidation() throws BusinessRuleValidationException {
        UserEmailValue email = new UserEmailValue("Test.User@Domain.Com ");
        assertEquals("test.user@domain.com", email.getValue());

        assertThrows(BusinessRuleValidationException.class, () -> new UserEmailValue("invalid-email"));
        assertThrows(BusinessRuleValidationException.class, () -> new UserEmailValue(""));
        assertThrows(BusinessRuleValidationException.class, () -> new UserEmailValue(null));
    }

    @Test
    void testCategoryNameAndAccountName() throws BusinessRuleValidationException {
        CategoryNameValue cat = new CategoryNameValue(" Food & Groceries ");
        assertEquals("Food & Groceries", cat.getName());

        assertThrows(BusinessRuleValidationException.class, () -> new CategoryNameValue("   "));

        AccountName acc = new AccountName("Main Savings");
        assertEquals("Main Savings", acc.getValue());
    }

    @Test
    void testDateRangeValidation() throws BusinessRuleValidationException {
        Date start = new Date(1000000);
        Date end = new Date(2000000);

        DateRange range = new DateRange(start, end);
        assertTrue(range.includes(new Date(1500000)));
        assertFalse(range.includes(new Date(500000)));

        assertThrows(BusinessRuleValidationException.class, () -> new DateRange(end, start));
    }

    @Test
    void testRules() {
        assertTrue(new NotNullRule("hello").isValid());
        assertFalse(new NotNullRule(null).isValid());

        assertTrue(new StringNotNullOrEmptyRule("ok").isValid());
        assertFalse(new StringNotNullOrEmptyRule("  ").isValid());

        assertTrue(new GreaterThanZeroRule(10.0, "amount").isValid());
        assertFalse(new GreaterThanZeroRule(0.0, "amount").isValid());
        assertFalse(new GreaterThanZeroRule(-5.0, "amount").isValid());

        UUID u1 = UUID.randomUUID();
        UUID u2 = UUID.randomUUID();
        assertTrue(new NotEqualRule<>(u1, u2, "Accounts must differ").isValid());
        assertFalse(new NotEqualRule<>(u1, u1, "Accounts must differ").isValid());
    }
}
