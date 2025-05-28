package com.finance.financier.command.aggregate;

import com.finance.financier.command.api.AcceptFinancingCommand;
import com.finance.financier.command.api.CancelFinancingCommand;
import com.finance.financier.command.api.DisburseFundsCommand;
import com.finance.financier.command.api.RequestFinancingCommand;
import com.finance.financier.event.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class FinancingAggregate {

    @AggregateIdentifier
    private String invoiceId;

    private boolean offered;
    private boolean accepted;
    private boolean disbursed;
    private boolean canceled;

    public FinancingAggregate() {}

    @CommandHandler
    public FinancingAggregate(RequestFinancingCommand cmd) {
        if (cmd.getOfferAmount() == null || cmd.getSupplierId() == null) {
            throw new IllegalArgumentException("Missing offer details");
        }

        apply(new FinancingOfferedEvent(
                cmd.getInvoiceId(),
                cmd.getSupplierId(),
                cmd.getOfferAmount(),
                cmd.getDiscountRate(),
                cmd.getExpiresOn()
        ));
    }

    @EventSourcingHandler
    public void on(FinancingOfferedEvent event) {
        this.invoiceId = event.getInvoiceId();
        this.offered = true;
        this.canceled = false;
        System.out.println("✅ Financing offer created: " + invoiceId);
    }

    @CommandHandler
    public void handle(AcceptFinancingCommand command) {
        if (!offered) {
            throw new IllegalStateException("No offer exists");
        }
        if (accepted) {
            throw new IllegalStateException("Offer already accepted");
        }
        apply(new FinancingAcceptedEvent(command.getInvoiceId()));
    }

    @EventSourcingHandler
    public void on(FinancingAcceptedEvent event) {
        this.accepted = true;
        System.out.println("✅ Financing accepted: " + event.getInvoiceId());
    }

    @CommandHandler
    public void handle(DisburseFundsCommand command) {
        if (!accepted) {
            throw new IllegalStateException("Offer not accepted yet");
        }
        if (disbursed) {
            throw new IllegalStateException("Already disbursed");
        }

        if (Math.random() > 0.2) {
            apply(new DisbursementCompletedEvent(command.getInvoiceId()));
        } else {
            apply(new DisbursementFailedEvent(command.getInvoiceId(), "Insufficient liquidity"));
        }
    }

    @EventSourcingHandler
    public void on(DisbursementCompletedEvent event) {
        this.disbursed = true;
        System.out.println("✅ Disbursement completed: " + event.getInvoiceId());
    }

    @CommandHandler
    public void handle(CancelFinancingCommand command) {
        if (canceled) {
            throw new IllegalStateException("Already canceled");
        }
        apply(new FinancingCanceledEvent(command.getInvoiceId()));
    }

    @EventSourcingHandler
    public void on(FinancingCanceledEvent event) {
        this.canceled = true;
        System.out.println("❌ Financing canceled: " + event.getInvoiceId());
    }
}
