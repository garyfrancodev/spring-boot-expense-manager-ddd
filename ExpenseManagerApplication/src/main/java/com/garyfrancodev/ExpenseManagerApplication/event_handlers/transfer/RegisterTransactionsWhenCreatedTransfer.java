package com.garyfrancodev.ExpenseManagerApplication.event_handlers.transfer;

import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import com.garyfrancodev.ExpenseManagerApplication.dtos.transaction.CreateTransactionDto;
import com.garyfrancodev.ExpenseManagerApplication.notifications.CreatedTransferNotification;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.transaction.command.CreateTransactionCommand;
import com.garyfrancodev.ExpenseManagerDomain.enums.TransactionType;
import com.garyfrancodev.ExpenseManagerDomain.events.transfer.CreatedTransfer;
import com.garyfrancodev.ExpenseManagerDomain.model.Category;
import com.garyfrancodev.ExpenseManagerDomain.repositories.CategoryRepository;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class RegisterTransactionsWhenCreatedTransfer implements Notification.Handler<CreatedTransferNotification> {

    @Autowired
    private Pipeline pipeline;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void handle(CreatedTransferNotification notification) {
        CreatedTransfer createdTransfer = notification.getEvent();

        Category expenseCategory = null;
        try {
            expenseCategory = categoryRepository.getCategoryDefaultByUserByCategoryType(createdTransfer.getUserId(), "DefaultExpense");
        } catch (BusinessRuleValidationException e) {
            throw new RuntimeException("Could not retrieve default expense category", e);
        }

        CreateTransactionDto transactionAccountSend = buildCreateTransactionDto(
                createdTransfer,
                createdTransfer.getAccountSend(),
                TransactionType.EXPENSE_TRANSFER,
                expenseCategory != null ? expenseCategory.getId() : null
        );
        new CreateTransactionCommand(transactionAccountSend).execute(pipeline);

        Category incomeCategory = null;
        try {
            incomeCategory = categoryRepository.getCategoryDefaultByUserByCategoryType(createdTransfer.getUserId(), "DefaultIncome");
        } catch (BusinessRuleValidationException e) {
            throw new RuntimeException("Could not retrieve default income category", e);
        }

        CreateTransactionDto transactionAccountReceives = buildCreateTransactionDto(
                createdTransfer,
                createdTransfer.getAccountReceives(),
                TransactionType.INCOME_TRANSFER,
                incomeCategory != null ? incomeCategory.getId() : null
        );
        new CreateTransactionCommand(transactionAccountReceives).execute(pipeline);
    }

    private CreateTransactionDto buildCreateTransactionDto(CreatedTransfer createdTransfer, UUID accountId, TransactionType transactionType, UUID categoryId) {
        CreateTransactionDto createTransactionDto = new CreateTransactionDto();
        createTransactionDto.setTransactionDescription(createdTransfer.getDescription() != null ? createdTransfer.getDescription() : "Transfer");
        createTransactionDto.setAmount(createdTransfer.getAmount());
        createTransactionDto.setUserId(createdTransfer.getUserId());
        createTransactionDto.setCategoryId(categoryId);
        createTransactionDto.setTransactionType(transactionType);
        createTransactionDto.setAccountId(accountId);
        createTransactionDto.setDate(createdTransfer.getOccurredOn() != null ? createdTransfer.getOccurredOn() : new Date());
        return createTransactionDto;
    }
}
