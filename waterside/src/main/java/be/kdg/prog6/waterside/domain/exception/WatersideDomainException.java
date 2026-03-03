package be.kdg.prog6.waterside.domain.exception;

import be.kdg.prog6.common.exception.InvalidOperationException;

/**
 * Base class for all exceptions specific to the Waterside domain.
 */
public class WatersideDomainException extends InvalidOperationException {
    public WatersideDomainException(final String message) {
        super(message);
    }
}
