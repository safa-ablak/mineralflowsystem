package be.kdg.prog6.warehousing.domain.exception.storage;

import be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException;

public class InvalidRawMaterialException extends WarehousingDomainException {
    public InvalidRawMaterialException(final String value) {
        super(value.isBlank()
            ? "Raw material name cannot be empty"
            : "Invalid Raw Material: %s".formatted(value));
    }
}
