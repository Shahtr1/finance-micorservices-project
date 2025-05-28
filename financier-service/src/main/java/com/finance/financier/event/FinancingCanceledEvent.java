package com.finance.financier.event;

public class FinancingCanceledEvent {
    private final String invoiceId;

    public FinancingCanceledEvent(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }
}
