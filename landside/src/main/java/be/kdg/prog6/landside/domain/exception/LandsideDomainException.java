package be.kdg.prog6.landside.domain.exception;

import be.kdg.prog6.common.exception.InvalidOperationException;

/**
 * Base class for all exceptions specific to the Landside domain.
 */
public class LandsideDomainException extends InvalidOperationException {
    public LandsideDomainException(final String message) {
        super(message);
    }
}
