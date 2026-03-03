package be.kdg.prog6.common.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(final String message) {
        super(message);
    }
}
