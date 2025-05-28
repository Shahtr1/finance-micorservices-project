package com.finance.financier.query.repository;

import com.finance.financier.query.entity.FinancingOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancingOfferRepository extends JpaRepository<FinancingOfferEntity, String> {
    List<FinancingOfferEntity> findBySupplierId(String supplierId);
}
