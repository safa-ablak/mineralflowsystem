package be.kdg.prog6.invoicing.adapter.in.web.controller;

import be.kdg.prog6.invoicing.adapter.in.web.dto.InvoiceDto;
import be.kdg.prog6.invoicing.domain.CustomerId;
import be.kdg.prog6.invoicing.domain.Invoice;
import be.kdg.prog6.invoicing.port.in.command.InvoiceCustomerCommand;
import be.kdg.prog6.invoicing.port.in.usecase.InvoiceCustomerUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static be.kdg.prog6.common.security.UserActivityLogger.logUserActivity;
import static java.lang.String.format;

@RestController
@RequestMapping("/invoice-customer")
public class InvoiceCustomerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceCustomerController.class);

    private final InvoiceCustomerUseCase invoiceCustomerUseCase;

    public InvoiceCustomerController(final InvoiceCustomerUseCase invoiceCustomerUseCase) {
        this.invoiceCustomerUseCase = invoiceCustomerUseCase;
    }

    /**
     * 📘 - User Story<br></br>
     * As an <b>accountant</b>, I want to invoice customers at request
     * so that I can ensure that the customer is billed for the outstanding credit.
     */
    @PostMapping("/{customer}")
    @PreAuthorize("hasAnyRole('ROLE_ACCOUNTANT', 'ROLE_ADMIN')")
    public ResponseEntity<InvoiceDto> invoiceCustomer(@PathVariable("customer") final UUID customerId,
                                                      @AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, format("is invoicing Customer with ID %s", customerId));
        final InvoiceCustomerCommand command = new InvoiceCustomerCommand(
            CustomerId.of(customerId)
        );
        final Invoice invoice = invoiceCustomerUseCase.invoiceCustomer(command);
        return ResponseEntity.ok(InvoiceDto.fromDomain(invoice));
    }
}