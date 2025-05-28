package com.finance.invoice.query.controller;

import com.finance.invoice.query.entity.InvoiceEntity;
import com.finance.invoice.query.repository.InvoiceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceQueryController {

    private final InvoiceRepository invoiceRepository;

    public InvoiceQueryController(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping
    public List<InvoiceEntity> getAll() {
        return invoiceRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceEntity> getById(@PathVariable String id) {
        return invoiceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
