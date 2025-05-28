## Kafka
After saving a `FinancingOfferEntity`, you want to:

-   Broadcast that offer to other systems
    -  So the supplier is notified
    -  So the audit/logging system can track it
    -  So the frontend can get real-time updates

You do this by publishing the event to a Kafka topic.

1. You save the offer to DB
```java
FinancingOfferEntity offer = new FinancingOfferEntity(...);
        financingOfferRepository.save(offer);
```

2. You send it to Kafka
```java
kafkaTemplate.send("financing-offers", event.getInvoiceId(), event);
```
`kafkaTemplate` is Spring’s helper for producing Kafka messages
`financing-offers` is the topic
event is the message — here, a `FinancingOfferedEvent`


## Kafka Message Structure

Kafka will serialize your `FinancingOfferedEvent` as JSON (thanks to JsonSerializer) and send it keyed by `invoiceId`.

This makes the message look something like:
```json
{
  "Key": "INV-12345",
  "Value": {
    "invoiceId": "INV-12345",
    "supplierId": "SUP-789",
    "offerAmount": 10000,
    "discountRate": 0.05,
    "expiresOn": "2025-06-15T00:00:00",
    ...
  }
}
```