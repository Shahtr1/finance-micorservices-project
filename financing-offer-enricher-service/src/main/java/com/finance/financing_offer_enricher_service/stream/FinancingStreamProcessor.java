package com.finance.financing_offer_enricher_service.stream;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.financing_offer_enricher_service.model.EnrichedFinancingOffer;
import com.finance.financing_offer_enricher_service.model.FinancingOfferedEvent;
import com.finance.financing_offer_enricher_service.model.SupplierProfile;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class FinancingStreamProcessor {

    private final ObjectMapper mapper = new ObjectMapper();

    @Bean
    public CommandLineRunner streamPipeline() {
        return args -> {
            Properties props = new Properties();
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, "financing-offer-enricher-app");
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

            StreamsBuilder builder = new StreamsBuilder();
            /*
            StreamsBuilder is the entry point to define a Kafka Streams topology.

            You use it to describe the flow:

            -   Which topics to read from
            -   How to transform data
            -   What to output to other topics

            Think of StreamsBuilder like a blueprint for building a Kafka pipeline before it runs.
            */

                    // 1. Read Financing Offers as raw JSON
            KStream<String, String> offers = builder.stream("financing-offers");
//            “Whenever a new financing offer is published to Kafka, this stream will pick it up and process it in real time.”

            // 2. Read Supplier Profiles from KTable (latest snapshot per supplier)
            KTable<String, String> supplierTable = builder.table("supplier-snapshots");

            // 3. Join Offer + Supplier
            KStream<String, EnrichedFinancingOffer> enrichedStream = offers.join(
                    supplierTable,
                    (offerJson, supplierJson) -> {
                        try {
                            FinancingOfferedEvent offer = mapper.readValue(offerJson, FinancingOfferedEvent.class);
                            SupplierProfile supplier = mapper.readValue(supplierJson, SupplierProfile.class);

                            boolean riskyRate = offer.getDiscountRate().doubleValue() > 0.18;
                            boolean highValue = offer.getOfferAmount().doubleValue() > 100000;
                            boolean notifyRM = riskyRate || highValue;
                            boolean premium = supplier.isPremium();

                            return new EnrichedFinancingOffer(
                                    offer.getInvoiceId(),
                                    offer.getSupplierId(),
                                    offer.getOfferAmount(),
                                    offer.getDiscountRate(),
                                    offer.getExpiresOn(),
                                    premium,
                                    riskyRate,
                                    notifyRM,
                                    supplier.getRiskScore()
                            );
                        } catch (Exception e) {
                            return null;
                        }
                    }
            );

            // 4. Filter null (failed deserialization)
            KStream<String, EnrichedFinancingOffer> validEnriched = enrichedStream.filter((k, v) -> v != null);

            // 5. Write to enriched-financing-offers
            validEnriched.mapValues(enriched -> {
                try {
                    return mapper.writeValueAsString(enriched);
                } catch (Exception e) {
                    return "{}";
                }
            }).to("enriched-financing-offers", Produced.with(Serdes.String(), Serdes.String()));

            // 6. Optional: Write flagged offers separately
            validEnriched.filter((k, v) -> v.isNotifyRelationshipManager())
                    .mapValues(v -> {
                        try {
                            return mapper.writeValueAsString(v);
                        } catch (Exception e) {
                            return "{}";
                        }
                    }).to("flagged-financing-offers", Produced.with(Serdes.String(), Serdes.String()));

            // Start the pipeline
            KafkaStreams streams = new KafkaStreams(builder.build(), props);
            streams.start();

            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        };
    }
}

