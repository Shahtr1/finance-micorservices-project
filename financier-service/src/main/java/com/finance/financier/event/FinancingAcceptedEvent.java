package com.finance.financier.event;

public class FinancingAcceptedEvent {
    private final String invoiceId;

    public FinancingAcceptedEvent(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }
}
