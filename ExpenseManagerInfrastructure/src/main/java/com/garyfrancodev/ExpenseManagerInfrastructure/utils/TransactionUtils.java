package com.garyfrancodev.ExpenseManagerInfrastructure.utils;

import com.garyfrancodev.ExpenseManagerDomain.model.Transaction;
import com.garyfrancodev.ExpenseManagerInfrastructure.model.TransactionJpaModel;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;

public class TransactionUtils {
    public static Transaction jpaModelToDomainModel(TransactionJpaModel transactionJpaModel) {
        try {
            return new Transaction(
                    transactionJpaModel.getAccount().getId(),
                    transactionJpaModel.getUser().getId(),
                    transactionJpaModel.getCategory().getId(),
                    transactionJpaModel.getAmount(),
                    transactionJpaModel.getCreated_at(),
                    transactionJpaModel.getDate(),
                    transactionJpaModel.getTransactionDescription(),
                    transactionJpaModel.getTransactionType(),
                    transactionJpaModel.getId()
            );
        } catch (BusinessRuleValidationException e) {
            throw new RuntimeException("Error mapping Transaction JPA to Domain model: " + e.getMessage(), e);
        }
    }
}
