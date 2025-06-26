package com.finance.financing_offer_enricher_service.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EnrichedFinancingOffer {
    private String invoiceId;
    private String supplierId;
    private BigDecimal offerAmount;
    private BigDecimal discountRate;
    private LocalDate expiresOn;

    private boolean premiumSupplier;
    private boolean riskyRate;
    private boolean notifyRelationshipManager;
    private int riskScore;

    public EnrichedFinancingOffer() {} // for JSON (de)serialization

    public EnrichedFinancingOffer(String invoiceId, String supplierId, BigDecimal offerAmount,
                                  BigDecimal discountRate, LocalDate expiresOn,
                                  boolean premiumSupplier, boolean riskyRate,
                                  boolean notifyRelationshipManager, int riskScore) {
        this.invoiceId = invoiceId;
        this.supplierId = supplierId;
        this.offerAmount = offerAmount;
        this.discountRate = discountRate;
        this.expiresOn = expiresOn;
        this.premiumSupplier = premiumSupplier;
        this.riskyRate = riskyRate;
        this.notifyRelationshipManager = notifyRelationshipManager;
        this.riskScore = riskScore;
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

    public boolean isPremiumSupplier() {
        return premiumSupplier;
    }

    public void setPremiumSupplier(boolean premiumSupplier) {
        this.premiumSupplier = premiumSupplier;
    }

    public boolean isRiskyRate() {
        return riskyRate;
    }

    public void setRiskyRate(boolean riskyRate) {
        this.riskyRate = riskyRate;
    }

    public boolean isNotifyRelationshipManager() {
        return notifyRelationshipManager;
    }

    public void setNotifyRelationshipManager(boolean notifyRelationshipManager) {
        this.notifyRelationshipManager = notifyRelationshipManager;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }
}
