package com.finance.invoice.command.api;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class CancelFinancingCommand {
    @TargetAggregateIdentifier
    private final String invoiceId;

    public CancelFinancingCommand(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }
}
