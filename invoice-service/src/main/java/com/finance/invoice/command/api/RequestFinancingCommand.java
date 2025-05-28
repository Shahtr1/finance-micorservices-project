package com.finance.invoice.command.api;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class RequestFinancingCommand {
    @TargetAggregateIdentifier
    private final String invoiceId;

    public RequestFinancingCommand(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }
}
