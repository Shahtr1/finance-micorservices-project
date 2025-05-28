package com.finance.invoice.command.api;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class ApproveInvoiceCommand {

    @TargetAggregateIdentifier
    private String invoiceId;

    public ApproveInvoiceCommand(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }
}
