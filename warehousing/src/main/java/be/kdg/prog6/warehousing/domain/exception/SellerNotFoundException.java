package be.kdg.prog6.warehousing.domain.exception;

import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.warehousing.domain.SellerId;

public class SellerNotFoundException extends NotFoundException {
    public SellerNotFoundException(final SellerId id) {
        super(
            String.format("Seller with ID %s not found", id.id())
        );
    }
}
