Diving into the core of Invoice and Supplier Finance, which involves three parties:

-   🧾 Supplier
-   🏢 Buyer
-   🏦 Bank / Financier

1. Supplier issues an invoice to Buyer

-   Supplier delivers goods/services.
-   Creates an invoice for $10,000 payable in 60 days.
-   Enters it into the platform.

2. Buyer verifies & approves the invoice

-   Buyer checks invoice details.
-   Approves it — this signals the invoice is legit and payable.

Now the invoice becomes financeable.

3. Bank/Financier evaluates and offers financing

-   Financier sees an approved invoice.
-   Offers to pay the supplier now (say, $9,800).
-   Deducts 2% as a discount (this is the financier’s income).
-   This is the Financing Offer.

Offer may expire in 2–5 days.

4. Supplier accepts the offer

-   Supplier chooses to take early cash.
-   Accepts the financing offer.

5. Bank disburses funds to supplier

-   Bank sends $9,800 to supplier’s account.
-   Supplier gets working capital immediately.

6. Buyer pays full invoice later

-   After 60 days, buyer pays $10,000 to the bank.
-   The bank earns $200.


## Mapping to Your Application Flow

1. Invoice Creation (Supplier)

-   `POST /api/invoices`
-   Triggers `CreateInvoiceCommand`
-   Event: `InvoiceCreatedEvent`
-   Stored in Axon & Read model

2. Invoice Approval (Buyer)

-   `POST /api/invoices/{id}/approve`
-   Triggers `ApproveInvoiceCommand`
-   Event: `InvoiceApprovedEvent`
-   Triggers `FinancingSaga`

3. Financing Offered (Bank)

-   Saga sends RequestFinancingCommand to financier-service
-   Aggregate creates FinancingOfferedEvent
-   Details:
    - `invoiceId`
    - `offerAmount = $9800`
    - `discountRate = 2%`
    - `expiresOn = T+3 days`

You store this in DB or send via Kafka/email to notify supplier.

4. Supplier Accepts

-   `POST /financing/{invoiceId}/accept`
-   Triggers `AcceptFinancingCommand`
-   Event: `FinancingAcceptedEvent`
-   Saga sends `DisburseFundsCommand`

5. Disbursement by Financier

-   Aggregate executes logic:
  -   If disbursement succeeds: `DisbursementCompletedEvent`
  -   Else: `DisbursementFailedEvent` → `CancelFinancingCommand`

6. Buyer Pays Later (Future scope)

-   Buyer pays bank after 60 days.
-   Can be tracked via a `RepaymentService`.
-   Event: `InvoiceSettledEvent`


