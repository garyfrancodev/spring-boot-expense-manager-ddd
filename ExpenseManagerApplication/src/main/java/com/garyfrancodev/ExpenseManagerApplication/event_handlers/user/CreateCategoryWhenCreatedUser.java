package com.garyfrancodev.ExpenseManagerApplication.event_handlers.user;

import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import com.garyfrancodev.ExpenseManagerApplication.dtos.category.CreateCategoryDto;
import com.garyfrancodev.ExpenseManagerApplication.notifications.CreatedUserEventNotification;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.category.command.CreateCategoryCommand;
import com.garyfrancodev.ExpenseManagerDomain.enums.CategoryType;
import com.garyfrancodev.ExpenseManagerDomain.events.user.CreatedUserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateCategoryWhenCreatedUser implements Notification.Handler<CreatedUserEventNotification> {

    @Autowired
    private Pipeline pipeline;

    @Override
    public void handle(CreatedUserEventNotification notification) {
        CreatedUserEvent createdUserEvent = notification.getEvent();
        UUID userId = createdUserEvent.getUserId();

        createCategory("Allowance", "gf-icon-allowance", "Allowance category", CategoryType.INCOME, userId);
        createCategory("Salary", "gf-icon-salary", "Salary category", CategoryType.INCOME, userId);
        createCategory("Bonus", "gf-icon-bonus", "Bonus category", CategoryType.INCOME, userId);
        createCategory("Food", "gf-icon-food", "Food category", CategoryType.EXPENSE, userId);
        createCategory("Education", "gf-icon-education", "Education category", CategoryType.EXPENSE, userId);
        createCategory("Gym", "gf-icon-gym", "Gym category", CategoryType.EXPENSE, userId);
        createCategory("DefaultExpense", "gf-icon-def", "Default category for expense transfers", CategoryType.EXPENSE, userId);
        createCategory("DefaultIncome", "gf-icon-def", "Default category for income transfers", CategoryType.INCOME, userId);
    }

    private void createCategory(String name, String icon, String description, CategoryType type, UUID userId) {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto(name, icon, description, type, userId);
        CreateCategoryCommand createCategoryCommand = new CreateCategoryCommand(createCategoryDto);
        createCategoryCommand.execute(pipeline);
    }
}
