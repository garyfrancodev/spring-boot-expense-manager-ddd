package com.garyfrancodev.ExpenseManagerApplication.event_handlers.transaction;

import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import com.garyfrancodev.ExpenseManagerApplication.notifications.CreatedTransactionNotification;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.account.command.UpdateAccountBalanceCommand;
import com.garyfrancodev.ExpenseManagerDomain.events.transaction.CreatedTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateAccountBalanceWhenCreatedTransaction implements Notification.Handler<CreatedTransactionNotification> {

    @Autowired
    private Pipeline pipeline;

    @Override
    public void handle(CreatedTransactionNotification notification) {
        CreatedTransaction createdTransaction = notification.getEvent();
        new UpdateAccountBalanceCommand(
                createdTransaction.getAccountId(),
                createdTransaction.getAmount(),
                createdTransaction.getTransactionType(),
                true
        ).execute(pipeline);
    }
}
