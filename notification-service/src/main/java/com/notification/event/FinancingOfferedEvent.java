package com.notification.event;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FinancingOfferedEvent {
    private final String invoiceId;
    private final String supplierId;
    private final BigDecimal offerAmount;
    private final BigDecimal discountRate;
    private final LocalDate expiresOn;

    public FinancingOfferedEvent(String invoiceId, String supplierId, BigDecimal offerAmount, BigDecimal discountRate, LocalDate expiresOn) {
        this.invoiceId = invoiceId;
        this.supplierId = supplierId;
        this.offerAmount = offerAmount;
        this.discountRate = discountRate;
        this.expiresOn = expiresOn;
    }

    public String getInvoiceId() { return invoiceId; }
    public String getSupplierId() { return supplierId; }
    public BigDecimal getOfferAmount() { return offerAmount; }
    public BigDecimal getDiscountRate() { return discountRate; }
    public LocalDate getExpiresOn() { return expiresOn; }
}
