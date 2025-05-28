package com.finance.financier.query.handler;

import com.finance.financier.event.FinancingOfferedEvent;
import com.finance.financier.query.entity.FinancingOfferEntity;
import com.finance.financier.query.repository.FinancingOfferRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class FinancingOfferProjection {

    private final FinancingOfferRepository offerRepository;
    private final KafkaTemplate<String, FinancingOfferedEvent> kafkaTemplate;

    public FinancingOfferProjection(FinancingOfferRepository offerRepository, KafkaTemplate<String, FinancingOfferedEvent> kafkaTemplate) {
        this.offerRepository = offerRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @EventHandler
    public void on(FinancingOfferedEvent event) {
        FinancingOfferEntity offer = new FinancingOfferEntity(
                event.getInvoiceId(),
                event.getSupplierId(),
                event.getOfferAmount(),
                event.getDiscountRate(),
                event.getExpiresOn(),
                "OFFERED"
        );
        offerRepository.save(offer);

        // 🔁 Publish to Kafka topic
        kafkaTemplate.send("financing-offers", event.getInvoiceId(), event);
        System.out.println("📢 Financing offer published to Kafka: " + event.getInvoiceId());
    }
}
