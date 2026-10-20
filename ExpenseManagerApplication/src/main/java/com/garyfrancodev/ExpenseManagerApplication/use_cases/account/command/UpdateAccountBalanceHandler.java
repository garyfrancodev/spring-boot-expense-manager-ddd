package com.garyfrancodev.ExpenseManagerApplication.use_cases.account.command;

import an.awesome.pipelinr.Command;
import com.garyfrancodev.ExpenseManagerDomain.enums.TransactionType;
import com.garyfrancodev.ExpenseManagerDomain.model.Account;
import com.garyfrancodev.ExpenseManagerDomain.repositories.AccountRepository;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
public class UpdateAccountBalanceHandler implements Command.Handler<UpdateAccountBalanceCommand, UUID> {
    private final AccountRepository _accountRepository;

    @Autowired
    public UpdateAccountBalanceHandler(AccountRepository accountRepository) {
        this._accountRepository = accountRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UUID handle(UpdateAccountBalanceCommand updateAccountBalanceCommand) {
        Account account = _accountRepository.getAccountById(updateAccountBalanceCommand.getAccountId());
        if (account == null) {
            throw new NoSuchElementException("Account not found with ID: " + updateAccountBalanceCommand.getAccountId());
        }

        TransactionType transactionType = updateAccountBalanceCommand.getTransactionType();
        try {
            if (updateAccountBalanceCommand.getFromNewTransaction()) {
                account.applyTransaction(transactionType, updateAccountBalanceCommand.getAmount());
            } else {
                account.applyTransactionDifference(transactionType, updateAccountBalanceCommand.getAmount());
            }
        } catch (BusinessRuleValidationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return _accountRepository.update(account);
    }
}
