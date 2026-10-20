package com.garyfrancodev.ExpenseManagerApplication.dispatcher;

import an.awesome.pipelinr.Pipeline;
import com.garyfrancodev.ExpenseManagerApplication.notifications.CreatedTransactionNotification;
import com.garyfrancodev.ExpenseManagerApplication.notifications.CreatedTransferNotification;
import com.garyfrancodev.ExpenseManagerApplication.notifications.CreatedUserEventNotification;
import com.garyfrancodev.ExpenseManagerApplication.notifications.UpdatedTransactionNotification;
import com.garyfrancodev.ExpenseManagerDomain.events.transaction.CreatedTransaction;
import com.garyfrancodev.ExpenseManagerDomain.events.transaction.UpdatedTransaction;
import com.garyfrancodev.ExpenseManagerDomain.events.transfer.CreatedTransfer;
import com.garyfrancodev.ExpenseManagerDomain.events.user.CreatedUserEvent;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.AggregateRoot;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.DomainEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DomainEventDispatcher {

    private final Pipeline pipeline;

    @Autowired
    public DomainEventDispatcher(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public void dispatchEventsFor(AggregateRoot aggregate) {
        if (aggregate == null) return;
        List<DomainEvent> events = aggregate.getDomainEvents();
        for (DomainEvent event : events) {
            dispatch(event);
        }
        aggregate.clearDomainEvents();
    }

    public void dispatch(DomainEvent event) {
        if (event == null) return;

        if (event instanceof CreatedTransaction createdTransaction) {
            new CreatedTransactionNotification(createdTransaction).send(pipeline);
        } else if (event instanceof UpdatedTransaction updatedTransaction) {
            new UpdatedTransactionNotification(updatedTransaction).send(pipeline);
        } else if (event instanceof CreatedTransfer createdTransfer) {
            new CreatedTransferNotification(createdTransfer).send(pipeline);
        } else if (event instanceof CreatedUserEvent createdUserEvent) {
            new CreatedUserEventNotification(createdUserEvent).send(pipeline);
        }
    }
}
