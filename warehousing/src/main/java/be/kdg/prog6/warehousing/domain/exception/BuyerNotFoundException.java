package be.kdg.prog6.warehousing.domain.exception;

import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.warehousing.domain.BuyerId;

public class BuyerNotFoundException extends NotFoundException {
    public BuyerNotFoundException(final BuyerId id) {
        super(String.format("Buyer with ID %s not found", id.id()));
    }
}
