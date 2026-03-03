package be.kdg.prog6.landside.domain.exception;

/**
 * Exception thrown when a truck arrives after its scheduled arrival window.
 */
public class LateArrivalException extends LandsideDomainException {
    public LateArrivalException(final String message) {
        super(message);
    }
}