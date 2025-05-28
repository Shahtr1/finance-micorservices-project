## Axon Framework = CQRS + Event Sourcing + DDD out-of-the-box

CQRS (Command Query Responsibility Segregation) says:
-   "Separate the parts of your system that handle commands (write operations) from the parts that handle queries (read operations)."

So instead of a single object/class doing both:

-   You have Aggregates (write model) for state-changing commands
-   And Projections (read model) for querying current state

We are applying:

- Command-side architecture (Write Model)
- Aggregate roots for domain behavior
- Event sourcing for traceability and state rebuild
- Will project to a Query-side (Read Model) later
- All changes flow through commands → events → projections

```plaintext
Client → POST /api/invoices
        ↓
CommandController → CreateInvoiceCommand
        ↓
Axon → finds InvoiceAggregate by ID
        ↓
InvoiceAggregate → emits InvoiceCreatedEvent
        ↓
Axon Server persists InvoiceCreatedEvent
        ↓
Aggregate rehydrates state with @EventSourcingHandler
        ↓
(Later) → Query side projection stores data in invoice_view table
        ↓
(Later) → Kafka event published for fraud, finance services
```


## DEEP FLOW

Step 1: `CommandGateway.send(...)`

When your controller calls:
```java
commandGateway.send(new CreateInvoiceCommand(...));
```
Behind the scenes:

-   Axon uses the command bus.
-   It sees the `@TargetAggregateIdentifier` (`invoiceId`) and uses that to route the command.
-   If this is the first command for a new ID, Axon will instantiate a new aggregate instance.

Think of this as:
```plaintext
AxonCommandBus.dispatch(command → handlerOnAggregate)
```

Step 2: `@CommandHandler `on `InvoiceAggregate`

```java
@CommandHandler
public InvoiceAggregate(CreateInvoiceCommand cmd) {
    apply(new InvoiceCreatedEvent(...));
}
```
Behind the scenes:

-   Axon invokes the constructor (or method) marked with `@CommandHandler`.
  -   `apply(...)` is called — this is not just calling your handler.
      What `apply(...)` really does:
      - Internally calls:
          -   `EventBus.publish(event)`
          -   `EventStore.append(event)`
          -   Triggers any `@EventSourcingHandler` in the aggregate
      -  Event is not persisted in a traditional DB — it's stored in **Axon Server** as a **stream**.

Step 3: Axon Server Stores the Event
```plaintext
apply(new InvoiceCreatedEvent(...)) 
→ triggers Axon internals:
   → serialize event
   → send to Axon Server
   → persist in event log (like Kafka but optimized for Axon)
```
**Axon Server** (or Axon Server SE/EE) stores events in an **append-only** event store like:

```markdown
EventStream: invoiceId=12345
  1. InvoiceCreatedEvent
  2. InvoiceApprovedEvent
  ...
```
-   Axon Server stores all events that happened to a particular Invoice aggregate instance (identified by invoiceId=12345).
-   Events are stored in an append-only fashion (immutable log).
-   Axon Server stores this event in the event store, tagged under invoiceId=12345.
-   These events represent every change the aggregate has gone through.
-   You can replay this stream at any time.

Step 4: Aggregate Rehydrates via `@EventSourcingHandler`
```java
@EventSourcingHandler
public void on(InvoiceCreatedEvent e) {
    this.invoiceId = e.getInvoiceId();
    ...
}
```
Axon is using event sourcing — instead of storing the current state of an object (like a row in a database), 
it stores all the events that led to that state.

So, when a command arrives:

-   Axon loads all past events for the aggregate ID.
-   It replays them using `@EventSourcingHandlers` to rebuild the current state.
-   Then it applies the new command logic based on that state.

## Won’t It Be Slow Over Time?
Yes, replaying hundreds or thousands of events every time would be slow. That’s why Axon has solutions:

1. Snapshotting (💡 Key optimization)

-   Axon can periodically save the current state of the aggregate as a snapshot.
-   Next time the aggregate loads, Axon starts from the snapshot + only replays new events.
-   You configure this in Axon easily.

```java
@Aggregate(snapshotTriggerDefinition = "mySnapshotTrigger")
public class Invoice { ... }
```

2. Caching
-   If you're using Axon Server EE or configure Axon to do so, 
-   aggregate caching can help by keeping some recently used aggregates in memory.

# Query Side — JPA Projection for Invoice Reads

This will let us:

-   Handle `InvoiceCreatedEvent`
-   Store invoice in a read model (PostgreSQL) for fast queries
-   Expose `/api/invoices/{id}` and `/api/invoices` endpoints

This is essential in CQRS because your write model (event store) is optimized for writes, not direct reads. 
The read model is denormalized and optimized for REST APIs.