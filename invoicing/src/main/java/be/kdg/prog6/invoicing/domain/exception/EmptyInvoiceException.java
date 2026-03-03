package be.kdg.prog6.invoicing.domain.exception;

import be.kdg.prog6.invoicing.domain.CustomerId;

public class EmptyInvoiceException extends InvoicingDomainException {
    private EmptyInvoiceException(String message) {
        super(message);
    }

    public static EmptyInvoiceException forCustomer(final CustomerId customerId) {
        return new EmptyInvoiceException(
            String.format("Cannot send Invoice for Customer %s: no Invoice Lines present",
                customerId.id()
            )
        );
    }
}
