package com.finance.financier.command.controller;

import com.finance.financier.command.api.AcceptFinancingCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/financing")
public class FinancingCommandController {

    private final CommandGateway commandGateway;

    public FinancingCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/{invoiceId}/accept")
    public ResponseEntity<String> acceptFinancing(@PathVariable String invoiceId) {
        commandGateway.send(new AcceptFinancingCommand(invoiceId));
        return ResponseEntity.ok("Financing accepted for: " + invoiceId);
    }

}
