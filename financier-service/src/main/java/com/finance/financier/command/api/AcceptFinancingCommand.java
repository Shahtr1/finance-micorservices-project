package com.finance.financier.command.api;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class AcceptFinancingCommand {
    @TargetAggregateIdentifier
    private final String invoiceId;

    public AcceptFinancingCommand(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }
}
