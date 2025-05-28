package com.finance.financier.query.controller;

import com.finance.financier.query.entity.FinancingOfferEntity;
import com.finance.financier.query.repository.FinancingOfferRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/financing-offers")
public class FinancingOfferController {

    private final FinancingOfferRepository repository;

    public FinancingOfferController(FinancingOfferRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<FinancingOfferEntity> getByInvoice(@PathVariable String invoiceId) {
        return repository.findById(invoiceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<FinancingOfferEntity> getBySupplier(@RequestParam String supplierId) {
        return repository.findBySupplierId(supplierId);
    }
}
