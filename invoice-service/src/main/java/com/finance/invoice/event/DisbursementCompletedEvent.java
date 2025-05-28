package com.finance.invoice.event;

public class DisbursementCompletedEvent {
    private final String invoiceId;

    public DisbursementCompletedEvent(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }
}
