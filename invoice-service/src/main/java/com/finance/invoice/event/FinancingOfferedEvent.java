package com.finance.invoice.event;

public class FinancingOfferedEvent {
    private final String invoiceId;

    public FinancingOfferedEvent(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }
}
