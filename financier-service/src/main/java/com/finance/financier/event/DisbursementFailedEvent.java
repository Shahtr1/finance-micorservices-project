package com.finance.financier.event;

public class DisbursementFailedEvent {
    private final String invoiceId;
    private final String reason;

    public DisbursementFailedEvent(String invoiceId, String reason) {
        this.invoiceId = invoiceId;
        this.reason = reason;
    }

    public String getInvoiceId() { return invoiceId; }
    public String getReason() { return reason; }
}
