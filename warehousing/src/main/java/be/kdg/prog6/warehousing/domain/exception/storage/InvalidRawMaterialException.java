package be.kdg.prog6.warehousing.domain.exception.storage;

import be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException;

public class InvalidRawMaterialException extends WarehousingDomainException {
    public InvalidRawMaterialException(final String message) {
        super(message);
    }
}
