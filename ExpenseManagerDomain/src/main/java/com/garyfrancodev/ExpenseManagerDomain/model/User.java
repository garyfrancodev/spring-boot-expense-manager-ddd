package com.garyfrancodev.ExpenseManagerDomain.model;

import com.garyfrancodev.ExpenseManagerDomain.events.user.CreatedUserEvent;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.AggregateRoot;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import com.garyfrancodev.ExpenseManagerSharedKernel.rules.StringNotNullOrEmptyRule;
import com.garyfrancodev.ExpenseManagerSharedKernel.valueobjects.UserEmailValue;

import java.util.Date;
import java.util.UUID;

/**
 * Aggregate Root representing a User.
 */
public class User extends AggregateRoot {
    private String firstName;
    private String lastName;
    private UserEmailValue email;
    private String password;
    private Date date;

    public User(String firstName, String lastName, String email, String password) throws BusinessRuleValidationException {
        super(UUID.randomUUID());
        checkRule(new StringNotNullOrEmptyRule(firstName, "First name"));
        checkRule(new StringNotNullOrEmptyRule(lastName, "Last name"));
        checkRule(new StringNotNullOrEmptyRule(password, "Password"));

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = new UserEmailValue(email);
        this.password = password;
        this.date = new Date();

        addDomainEvent(new CreatedUserEvent(this.id, this.email.getValue(), this.firstName, this.lastName, this.date));
    }

    public User(String firstName, String lastName, String email, String password, UUID id) throws BusinessRuleValidationException {
        super(id);
        checkRule(new StringNotNullOrEmptyRule(firstName, "First name"));
        checkRule(new StringNotNullOrEmptyRule(lastName, "Last name"));
        checkRule(new StringNotNullOrEmptyRule(password, "Password"));

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = new UserEmailValue(email);
        this.password = password;
        this.date = new Date();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email != null ? email.getValue() : null;
    }

    public UserEmailValue getEmailValue() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Date getDate() {
        return date;
    }
}
