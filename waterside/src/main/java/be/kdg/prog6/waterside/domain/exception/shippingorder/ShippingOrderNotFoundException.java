package be.kdg.prog6.waterside.domain.exception.shippingorder;

import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.waterside.domain.ReferenceId;
import be.kdg.prog6.waterside.domain.ShippingOrderId;

public class ShippingOrderNotFoundException extends NotFoundException {
    private ShippingOrderNotFoundException(final String message) {
        super(message);
    }

    public static ShippingOrderNotFoundException forId(final ShippingOrderId id) {
        return new ShippingOrderNotFoundException(
            String.format("Shipping Order with ID %s not found", id.id())
        );
    }

    public static ShippingOrderNotFoundException forReferenceId(final ReferenceId referenceId) {
        return new ShippingOrderNotFoundException(
            String.format("Shipping Order with Reference ID %s not found", referenceId.id())
        );
    }
}