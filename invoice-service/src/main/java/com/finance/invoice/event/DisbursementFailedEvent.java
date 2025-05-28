package com.finance.invoice.event;

public class DisbursementFailedEvent {
    private final String invoiceId;

    public DisbursementFailedEvent(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }
}
