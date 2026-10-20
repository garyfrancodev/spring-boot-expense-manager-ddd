package com.garyfrancodev.ExpenseManagerDomain.events.user;

import com.garyfrancodev.ExpenseManagerSharedKernel.core.DomainEvent;

import java.util.Date;
import java.util.UUID;

/**
 * Domain Event raised when a new user registers in the system.
 */
public class CreatedUserEvent extends DomainEvent {
    private final UUID userId;
    private final String email;
    private final String firstName;
    private final String lastName;

    public CreatedUserEvent(UUID userId, String email, String firstName, String lastName, Date date) {
        super(date);
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public CreatedUserEvent(UUID userId, Date date) {
        this(userId, null, null, null, date);
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
