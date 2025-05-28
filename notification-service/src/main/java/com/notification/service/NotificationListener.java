package com.notification.service;

import com.notification.event.FinancingOfferedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    @KafkaListener(topics = "financing-offers", groupId = "notification-group")
    public void onOffer(FinancingOfferedEvent event) {
        // Log simulated notification
        System.out.println("📧 Email to supplier " + event.getSupplierId());
        System.out.println("You have a new financing offer:");
        System.out.println("Invoice: " + event.getInvoiceId());
        System.out.println("Amount: $" + event.getOfferAmount());
        System.out.println("Rate: " + event.getDiscountRate() + "%");
        System.out.println("Expires on: " + event.getExpiresOn());

        // (Optional) Send real email using Spring Mail
    }
}
