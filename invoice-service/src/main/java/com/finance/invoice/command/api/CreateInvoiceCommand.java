package com.finance.invoice.command.api;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateInvoiceCommand {

    @TargetAggregateIdentifier
    private String invoiceId;

    private String supplierId;
    private String buyerId;
    private BigDecimal amount;
    private LocalDate dueDate;

    public CreateInvoiceCommand() {
    }

    public CreateInvoiceCommand(String invoiceId, String supplierId, String buyerId, BigDecimal amount, LocalDate dueDate) {
        this.invoiceId = invoiceId;
        this.supplierId = supplierId;
        this.buyerId = buyerId;
        this.amount = amount;
        this.dueDate = dueDate;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}
