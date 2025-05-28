package com.finance.financier.command.api;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class DisburseFundsCommand {
    @TargetAggregateIdentifier
    private final String invoiceId;

    public DisburseFundsCommand(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceId() { return invoiceId; }
}
