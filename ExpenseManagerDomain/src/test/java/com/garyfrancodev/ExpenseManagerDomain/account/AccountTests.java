package com.garyfrancodev.ExpenseManagerDomain.account;

import com.garyfrancodev.ExpenseManagerDomain.enums.TransactionType;
import com.garyfrancodev.ExpenseManagerDomain.model.Account;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTests {
    private static final UUID ID = UUID.randomUUID();
    private static final UUID USER_ID = UUID.randomUUID();

    @Test
    void testAccountCreation() {
        assertDoesNotThrow(() -> {
            Account account = new Account("BCP", "banco", 60.00, USER_ID);
            assertNotNull(account.getId());
            assertNotEquals(account.getId(), account.getUserId());
            assertEquals(60.00, account.getAmount());
            assertEquals("BCP", account.getAccountName());
        });
    }

    @Test
    void testAccountCreationWithId() throws BusinessRuleValidationException {
        Account account = new Account("BCP", "banco", 60.00, USER_ID, ID);
        assertNotNull(account.getId());
        assertEquals(ID, account.getId());
    }

    @Test
    void testNegativeInitialBalanceThrows() {
        assertThrows(BusinessRuleValidationException.class, () ->
                new Account("BCP", "banco", -10.00, USER_ID));
    }

    @Test
    void testEmptyAccountNameThrows() {
        assertThrows(BusinessRuleValidationException.class, () ->
                new Account("   ", "banco", 10.00, USER_ID));
    }

    @Test
    void testDepositAndWithdraw() throws BusinessRuleValidationException {
        Account account = new Account("Main", "Savings", 100.00, USER_ID);
        account.deposit(50.00);
        assertEquals(150.00, account.getAmount());

        account.withdraw(30.00);
        assertEquals(120.00, account.getAmount());

        assertThrows(BusinessRuleValidationException.class, () -> account.withdraw(200.00));
    }

    @Test
    void testApplyTransaction() throws BusinessRuleValidationException {
        Account account = new Account("Main", "Checking", 200.00, USER_ID);

        account.applyTransaction(TransactionType.EXPENSE, 50.00);
        assertEquals(150.00, account.getAmount());

        account.applyTransaction(TransactionType.INCOME, 100.00);
        assertEquals(250.00, account.getAmount());
    }
}
