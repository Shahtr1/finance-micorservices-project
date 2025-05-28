package com.finance.invoice.query.repository;

import com.finance.invoice.query.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, String> {
}
