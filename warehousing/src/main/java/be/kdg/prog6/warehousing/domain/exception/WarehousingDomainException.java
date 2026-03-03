package be.kdg.prog6.warehousing.domain.exception;

import be.kdg.prog6.common.exception.InvalidOperationException;

/**
 * Base class for all exceptions specific to the Warehousing domain.
 */
public class WarehousingDomainException extends InvalidOperationException {
    public WarehousingDomainException(final String message) {
        super(message);
    }
}
