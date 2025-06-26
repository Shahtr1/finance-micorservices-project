package com.finance.financing_offer_enricher_service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.financing_offer_enricher_service.model.SupplierProfile;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class MockSupplierProducer {

    @Bean
    public CommandLineRunner sendMockSuppliers() {
        return args -> {
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

            KafkaProducer<String, String> producer = new KafkaProducer<>(props);
            ObjectMapper mapper = new ObjectMapper();

            List<SupplierProfile> mockSuppliers = List.of(
                    new SupplierProfile("SUP-101", "Acme Corp", true, 780),
                    new SupplierProfile("SUP-102", "Globex Ltd", false, 640),
                    new SupplierProfile("SUP-103", "Wayne Tech", true, 710),
                    new SupplierProfile("SUP-104", "Initech", false, 590)
            );

            for (SupplierProfile supplier : mockSuppliers) {
                String key = supplier.getSupplierId();
                String value = mapper.writeValueAsString(supplier);
                ProducerRecord<String, String> record = new ProducerRecord<>("supplier-snapshots", key, value);
                producer.send(record);
            }

            producer.flush();
            producer.close();
        };
    }
}
