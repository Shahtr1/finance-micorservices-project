package com.finance.invoice.command.controller;

import com.finance.invoice.command.api.ApproveInvoiceCommand;
import com.finance.invoice.command.api.CreateInvoiceCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceCommandController {

    private final CommandGateway commandGateway;

    public InvoiceCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public ResponseEntity<String> createInvoice(@RequestParam String supplierId,
                                                @RequestParam String buyerId,
                                                @RequestParam BigDecimal amount,
                                                @RequestParam String dueDate) {

        String invoiceId = UUID.randomUUID().toString();

        CreateInvoiceCommand command = new CreateInvoiceCommand(
                invoiceId,
                supplierId,
                buyerId,
                amount,
                LocalDate.parse(dueDate)
        );

        commandGateway.sendAndWait(command);

        return ResponseEntity.ok("Invoice Created: " + invoiceId);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveInvoice(@PathVariable String id) {
        commandGateway.sendAndWait(new ApproveInvoiceCommand(id));
        return ResponseEntity.ok("Invoice approved: " + id);
    }

}
