package be.kdg.prog6.landside.domain.exception;

/**
 * Exception thrown when a truck arrives earlier than its scheduled window.
 */
public class EarlyArrivalException extends LandsideDomainException {
    public EarlyArrivalException(final String message) {
        super(message);
    }
}
