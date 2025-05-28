package com.finance.invoice.event;

public class InvoiceApprovedEvent {

    private final String invoiceId;

    public InvoiceApprovedEvent(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }
}
