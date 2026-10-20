package com.garyfrancodev.ExpenseManagerApplication.notifications;

import an.awesome.pipelinr.Notification;
import com.garyfrancodev.ExpenseManagerDomain.events.transaction.CreatedTransaction;

public class CreatedTransactionNotification implements Notification {
    private final CreatedTransaction event;

    public CreatedTransactionNotification(CreatedTransaction event) {
        this.event = event;
    }

    public CreatedTransaction getEvent() {
        return event;
    }
}
