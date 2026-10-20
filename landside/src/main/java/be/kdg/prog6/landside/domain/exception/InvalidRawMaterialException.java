package be.kdg.prog6.landside.domain.exception;

public class InvalidRawMaterialException extends LandsideDomainException {
    public InvalidRawMaterialException(final String value) {
        super("Invalid Raw Material type provided: " + value);
    }
}
