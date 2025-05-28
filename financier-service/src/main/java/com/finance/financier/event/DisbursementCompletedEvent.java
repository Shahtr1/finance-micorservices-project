package com.finance.financier.event;

public class DisbursementCompletedEvent {
    private final String invoiceId;

    public DisbursementCompletedEvent(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceId() { return invoiceId; }
}
