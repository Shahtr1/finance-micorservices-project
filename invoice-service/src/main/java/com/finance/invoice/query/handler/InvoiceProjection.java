package com.finance.invoice.query.handler;

import com.finance.invoice.event.InvoiceApprovedEvent;
import com.finance.invoice.event.InvoiceCreatedEvent;
import com.finance.invoice.query.entity.InvoiceEntity;
import com.finance.invoice.query.repository.InvoiceRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class InvoiceProjection {

    private final InvoiceRepository invoiceRepository;

    public InvoiceProjection(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @EventHandler
    public void on(InvoiceCreatedEvent event) {
        InvoiceEntity invoice = new InvoiceEntity(
                event.getInvoiceId(),
                event.getSupplierId(),
                event.getBuyerId(),
                event.getAmount(),
                event.getDueDate()
        );
        invoiceRepository.save(invoice);
        System.out.println("📦 Projection updated: " + event.getInvoiceId());
    }

    @EventHandler
    public void on(InvoiceApprovedEvent event) {
        InvoiceEntity invoice = invoiceRepository.findById(event.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        invoice.setStatus("APPROVED");
        invoiceRepository.save(invoice);
        System.out.println("✅ Invoice approved: " + event.getInvoiceId());
    }
}
