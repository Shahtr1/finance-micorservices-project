
1. 🏦 FinancierService emits a Kafka Event
```java
kafkaTemplate.send("financing-offers", invoiceId, event);
```

This does 3 things:

✅ Serializes your FinancingOfferedEvent into JSON
✅ Sends it to the Kafka cluster
✅ Kafka stores this message in the topic financing-offers under key = invoiceId

Think of Kafka as an append-only distributed log. This event is now stored reliably, like a new entry in a ledger.

2. 🔁 Kafka Streams App Is a Smart "Middleman"

Kafka Streams listens to this topic:

```java
KStream<String, String> offers = builder.stream("financing-offers");
```

So when a new event is published:

🔸 The Streams app receives it in real time
🔸 It joins it with latest SupplierProfile info
🔸 Applies logic (e.g., riskyRate, highValue, etc.)
🔸 Creates a new, enriched event
🔸 And sends that to another Kafka topic: enriched-financing-offers

3. 📬 NotificationService Listens to Processed Topic
   Instead of listening to raw data, it now listens to:
```java
@KafkaListener(topics = "enriched-financing-offers")
```

"We used Kafka to decouple the services. Our FinancierService only emits raw `FinancingOfferedEvent`.
We use a Kafka Streams microservice to enrich this data in real time by joining with Supplier Profiles, 
applying risk and business logic, and routing to separate topics like `enriched-financing-offers` and `flagged-financing-offers`.

This allows downstream systems — like the Notification service — 
to remain stateless and simple, while Kafka handles durability, distribution, and streaming logic."