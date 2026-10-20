package com.garyfrancodev.ExpenseManagerApplication.use_cases.transaction.queries;

import an.awesome.pipelinr.Command;
import com.garyfrancodev.ExpenseManagerApplication.dtos.transaction.TransactionDto;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import com.garyfrancodev.ExpenseManagerSharedKernel.valueobjects.DateRange;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class GetTransactionsByUserIdByPeriodQuery implements Command<List<TransactionDto>> {
    private final UUID userId;
    private final DateRange period;

    public GetTransactionsByUserIdByPeriodQuery(UUID userId, Date startDate, Date endDate) {
        this.userId = userId;
        try {
            this.period = new DateRange(startDate, endDate);
        } catch (BusinessRuleValidationException e) {
            throw new IllegalArgumentException("Invalid date period: " + e.getMessage(), e);
        }
    }

    public UUID getUserId() {
        return userId;
    }

    public Date getStartDate() {
        return period.getStartDate();
    }

    public Date getEndDate() {
        return period.getEndDate();
    }

    public DateRange getPeriod() {
        return period;
    }
}
