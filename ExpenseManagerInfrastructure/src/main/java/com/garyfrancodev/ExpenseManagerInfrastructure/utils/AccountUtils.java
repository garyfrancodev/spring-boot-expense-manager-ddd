package com.garyfrancodev.ExpenseManagerInfrastructure.utils;

import com.garyfrancodev.ExpenseManagerDomain.model.Account;
import com.garyfrancodev.ExpenseManagerInfrastructure.model.AccountJpaModel;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;

public class AccountUtils {
    public static Account jpaModelToDomainModel(AccountJpaModel accountJpaModel) {
        try {
            return new Account(
                    accountJpaModel.getAccountName(),
                    accountJpaModel.getDescription(),
                    accountJpaModel.getAmount(),
                    accountJpaModel.getUser() != null ? accountJpaModel.getUser().getId() : null,
                    accountJpaModel.getId()
            );
        } catch (BusinessRuleValidationException e) {
            throw new RuntimeException("Error mapping Account JPA to Domain model: " + e.getMessage(), e);
        }
    }
}
