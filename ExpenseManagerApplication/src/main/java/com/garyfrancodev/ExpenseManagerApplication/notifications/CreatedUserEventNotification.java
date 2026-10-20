package com.garyfrancodev.ExpenseManagerApplication.notifications;

import an.awesome.pipelinr.Notification;
import com.garyfrancodev.ExpenseManagerDomain.events.user.CreatedUserEvent;

public class CreatedUserEventNotification implements Notification {
    private final CreatedUserEvent event;

    public CreatedUserEventNotification(CreatedUserEvent event) {
        this.event = event;
    }

    public CreatedUserEvent getEvent() {
        return event;
    }
}
