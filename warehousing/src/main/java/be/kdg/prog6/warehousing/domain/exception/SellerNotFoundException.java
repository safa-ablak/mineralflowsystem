package be.kdg.prog6.warehousing.domain.exception;

import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.warehousing.domain.SellerId;

public class SellerNotFoundException extends NotFoundException {
    private SellerNotFoundException(String message) {
        super(message);
    }

    public static SellerNotFoundException forId(final SellerId id) {
        return new SellerNotFoundException(
            String.format("Seller with ID %s not found", id.id())
        );
    }
}
