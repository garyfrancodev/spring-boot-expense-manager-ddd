package com.garyfrancodev.ExpenseManagerApplication.notifications;

import an.awesome.pipelinr.Notification;
import com.garyfrancodev.ExpenseManagerDomain.events.transfer.CreatedTransfer;

public class CreatedTransferNotification implements Notification {
    private final CreatedTransfer event;

    public CreatedTransferNotification(CreatedTransfer event) {
        this.event = event;
    }

    public CreatedTransfer getEvent() {
        return event;
    }
}
