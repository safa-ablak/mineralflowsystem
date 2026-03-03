package be.kdg.prog6.invoicing.domain.exception;

import be.kdg.prog6.common.exception.InvalidOperationException;

/**
 * Base class for all exceptions specific to the Invoicing domain.
 */
public class InvoicingDomainException extends InvalidOperationException {
    public InvoicingDomainException(final String message) {
        super(message);
    }
}
