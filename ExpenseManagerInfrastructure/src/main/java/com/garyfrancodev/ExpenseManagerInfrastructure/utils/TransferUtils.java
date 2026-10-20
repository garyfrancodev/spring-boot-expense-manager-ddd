package com.garyfrancodev.ExpenseManagerInfrastructure.utils;

import com.garyfrancodev.ExpenseManagerDomain.model.Transfer;
import com.garyfrancodev.ExpenseManagerInfrastructure.model.TransferJpaModel;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;

public class TransferUtils {
    public static TransferJpaModel domainModelToJpa(Transfer transfer) {
        TransferJpaModel transactionJpaModel = new TransferJpaModel();
        transactionJpaModel.setDate(transfer.getDate());
        transactionJpaModel.setId(transfer.getId());
        transactionJpaModel.setAmount(transfer.getAmount());

        return transactionJpaModel;
    }

    public static Transfer jpaModelToDomainModel(TransferJpaModel transferJpaModel) {
        try {
            return new Transfer(
                    transferJpaModel.getAccountSend().getId(),
                    transferJpaModel.getAccountReceives().getId(),
                    transferJpaModel.getAmount(),
                    transferJpaModel.getDate(),
                    transferJpaModel.getUser().getId(),
                    transferJpaModel.getId()
            );
        } catch (BusinessRuleValidationException e) {
            throw new RuntimeException("Error mapping Transfer JPA to Domain model: " + e.getMessage(), e);
        }
    }
}
