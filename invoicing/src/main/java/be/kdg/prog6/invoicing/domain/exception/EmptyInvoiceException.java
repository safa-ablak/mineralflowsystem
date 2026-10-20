package be.kdg.prog6.invoicing.domain.exception;

import be.kdg.prog6.invoicing.domain.CustomerId;

public class EmptyInvoiceException extends InvoicingDomainException {
    public EmptyInvoiceException(final CustomerId customerId) {
        super(
            String.format("Cannot send Invoice for Customer %s: no Invoice Lines present",
                customerId.id()
            )
        );
    }
}
