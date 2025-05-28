package com.finance.financier.query.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "financing_offers")
public class FinancingOfferEntity {

    @Id
    private String invoiceId;

    private String supplierId;
    private BigDecimal offerAmount;
    private BigDecimal discountRate;
    private LocalDate expiresOn;
    private String status; // OFFERED, ACCEPTED, REJECTED

    public FinancingOfferEntity() {}

    public FinancingOfferEntity(String invoiceId, String supplierId, BigDecimal offerAmount, BigDecimal discountRate, LocalDate expiresOn, String status) {
        this.invoiceId = invoiceId;
        this.supplierId = supplierId;
        this.offerAmount = offerAmount;
        this.discountRate = discountRate;
        this.expiresOn = expiresOn;
        this.status = status;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public BigDecimal getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(BigDecimal offerAmount) {
        this.offerAmount = offerAmount;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public LocalDate getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(LocalDate expiresOn) {
        this.expiresOn = expiresOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
