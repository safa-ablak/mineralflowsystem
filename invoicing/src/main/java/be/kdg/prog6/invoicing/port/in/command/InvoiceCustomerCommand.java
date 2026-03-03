package be.kdg.prog6.invoicing.port.in.command;

import be.kdg.prog6.invoicing.domain.CustomerId;

import static java.util.Objects.requireNonNull;

public record InvoiceCustomerCommand(CustomerId customerId) {
    public InvoiceCustomerCommand {
        requireNonNull(customerId);
    }
}
