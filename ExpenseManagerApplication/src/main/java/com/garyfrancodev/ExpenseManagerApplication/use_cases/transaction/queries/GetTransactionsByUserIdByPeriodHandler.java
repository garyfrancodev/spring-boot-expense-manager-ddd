package com.garyfrancodev.ExpenseManagerApplication.use_cases.transaction.queries;

import an.awesome.pipelinr.Command;
import com.garyfrancodev.ExpenseManagerApplication.dtos.transaction.TransactionDto;
import com.garyfrancodev.ExpenseManagerDomain.model.Transaction;
import com.garyfrancodev.ExpenseManagerDomain.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetTransactionsByUserIdByPeriodHandler implements Command.Handler<GetTransactionsByUserIdByPeriodQuery, List<TransactionDto>> {

    private final TransactionRepository _transactionRepository;

    @Autowired
    public GetTransactionsByUserIdByPeriodHandler(TransactionRepository transactionRepository) {
        this._transactionRepository = transactionRepository;
    }

    @Override
    public List<TransactionDto> handle(GetTransactionsByUserIdByPeriodQuery query) {
        List<Transaction> transactionList = _transactionRepository.transactionListByUserIdByPeriod(
                query.getUserId(),
                query.getStartDate(),
                query.getEndDate()
        );
        if (transactionList == null) {
            return List.of();
        }
        return transactionList.stream()
                .map(transaction -> new TransactionDto(
                        transaction.getAccountId(),
                        transaction.getUserId(),
                        transaction.getAmount(),
                        transaction.getDate(),
                        transaction.getTransactionDescription(),
                        transaction.getTransactionType(),
                        transaction.getId()
                ))
                .toList();
    }
}
