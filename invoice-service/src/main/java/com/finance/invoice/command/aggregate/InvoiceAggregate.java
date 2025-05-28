package com.finance.invoice.command.aggregate;

import com.finance.invoice.command.api.ApproveInvoiceCommand;
import com.finance.invoice.command.api.CreateInvoiceCommand;
import com.finance.invoice.event.InvoiceApprovedEvent;
import com.finance.invoice.event.InvoiceCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class InvoiceAggregate {

    @AggregateIdentifier
    private String invoiceId;

    private String supplierId;
    private String buyerId;
    private BigDecimal amount;
    private LocalDate dueDate;

    private boolean approved = false;

    public InvoiceAggregate() {
    }

    @CommandHandler
    public InvoiceAggregate(CreateInvoiceCommand cmd) {
        apply(new InvoiceCreatedEvent(
                cmd.getInvoiceId(),
                cmd.getSupplierId(),
                cmd.getBuyerId(),
                cmd.getAmount(),
                cmd.getDueDate()
        ));
    }

    @EventSourcingHandler
    public void on(InvoiceCreatedEvent event) {
        this.invoiceId = event.getInvoiceId();
        this.supplierId = event.getSupplierId();
        this.buyerId = event.getBuyerId();
        this.amount = event.getAmount();
        this.dueDate = event.getDueDate();
    }

    @CommandHandler
    public void handle(ApproveInvoiceCommand command) {
        if (approved) {
            throw new IllegalStateException("Invoice is already approved");
        }
        apply(new InvoiceApprovedEvent(command.getInvoiceId()));
    }

    @EventSourcingHandler
    public void on(InvoiceApprovedEvent event) {
        this.approved = true;
    }
}
