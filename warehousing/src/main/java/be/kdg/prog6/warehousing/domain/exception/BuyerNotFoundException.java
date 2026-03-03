package be.kdg.prog6.warehousing.domain.exception;

import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.warehousing.domain.BuyerId;

public class BuyerNotFoundException extends NotFoundException {
    private BuyerNotFoundException(String message) {
        super(message);
    }

    public static BuyerNotFoundException forId(final BuyerId id) {
        return new BuyerNotFoundException(
            String.format("Buyer with ID %s not found", id.id())
        );
    }
}
