package com.finance.invoice.saga;


import com.finance.invoice.command.api.CancelFinancingCommand;
import com.finance.invoice.command.api.DisburseFundsCommand;
import com.finance.invoice.command.api.RequestFinancingCommand;
import com.finance.invoice.event.*;
import jakarta.persistence.Transient;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.*;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import static org.axonframework.modelling.saga.SagaLifecycle.*;

@Saga
public class FinancingSaga {

    @Transient
    private CommandGateway commandGateway;

    @Autowired
    public void setCommandGateway(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "invoiceId")
    public void on(InvoiceApprovedEvent event) {
        System.out.println("Saga started for invoice: " + event.getInvoiceId());
        associateWith("invoiceId", event.getInvoiceId());

        commandGateway.send(new RequestFinancingCommand(event.getInvoiceId()));
    }

    @SagaEventHandler(associationProperty = "invoiceId")
    public void on(FinancingOfferedEvent event) {
        System.out.println("Financing offer received: " + event.getInvoiceId());
        // Here you'd wait for supplier to accept
    }

    @SagaEventHandler(associationProperty = "invoiceId")
    public void on(FinancingAcceptedEvent event) {
        System.out.println("Financing accepted: " + event.getInvoiceId());
        commandGateway.send(new DisburseFundsCommand(event.getInvoiceId()));
    }

    @SagaEventHandler(associationProperty = "invoiceId")
    public void on(DisbursementCompletedEvent event) {
        System.out.println("Funds disbursed successfully for: " + event.getInvoiceId());
        end(); // Saga complete
    }

    @SagaEventHandler(associationProperty = "invoiceId")
    public void on(DisbursementFailedEvent event) {
        System.out.println("Disbursement failed. Sending cancel for: " + event.getInvoiceId());
        commandGateway.send(new CancelFinancingCommand(event.getInvoiceId()));
    }
}
