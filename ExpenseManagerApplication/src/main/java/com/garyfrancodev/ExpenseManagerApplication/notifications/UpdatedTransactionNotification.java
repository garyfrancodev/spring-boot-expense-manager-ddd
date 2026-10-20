package com.garyfrancodev.ExpenseManagerApplication.notifications;

import an.awesome.pipelinr.Notification;
import com.garyfrancodev.ExpenseManagerDomain.events.transaction.UpdatedTransaction;

public class UpdatedTransactionNotification implements Notification {
    private final UpdatedTransaction event;

    public UpdatedTransactionNotification(UpdatedTransaction event) {
        this.event = event;
    }

    public UpdatedTransaction getEvent() {
        return event;
    }
}
