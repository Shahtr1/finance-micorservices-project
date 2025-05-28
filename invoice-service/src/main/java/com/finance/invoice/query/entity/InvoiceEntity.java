package com.finance.invoice.query.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "invoices")
public class InvoiceEntity {

    @Id
    private String invoiceId;

    private String supplierId;
    private String buyerId;
    private BigDecimal amount;
    private LocalDate dueDate;
    private String status = "CREATED";

    public InvoiceEntity() {
    }

    public InvoiceEntity(String invoiceId, String supplierId, String buyerId, BigDecimal amount, LocalDate dueDate) {
        this.invoiceId = invoiceId;
        this.supplierId = supplierId;
        this.buyerId = buyerId;
        this.amount = amount;
        this.dueDate = dueDate;
        this.status = "CREATED";
    }

    // Getters and Setters
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
