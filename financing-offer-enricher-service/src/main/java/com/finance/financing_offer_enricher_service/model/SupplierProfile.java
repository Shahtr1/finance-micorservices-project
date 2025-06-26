package com.finance.financing_offer_enricher_service.model;

public class SupplierProfile {
    private String supplierId;
    private String name;
    private boolean premium;
    private int riskScore;

    public SupplierProfile() {} // for deserialization

    public SupplierProfile(String supplierId, String name, boolean premium, int riskScore) {
        this.supplierId = supplierId;
        this.name = name;
        this.premium = premium;
        this.riskScore = riskScore;
    }

    public String getSupplierId() { return supplierId; }
    public String getName() { return name; }
    public boolean isPremium() { return premium; }
    public int getRiskScore() { return riskScore; }

    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public void setName(String name) { this.name = name; }
    public void setPremium(boolean premium) { this.premium = premium; }
    public void setRiskScore(int riskScore) { this.riskScore = riskScore; }
}
