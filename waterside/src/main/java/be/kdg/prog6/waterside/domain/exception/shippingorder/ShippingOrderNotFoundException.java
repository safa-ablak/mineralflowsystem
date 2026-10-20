package be.kdg.prog6.waterside.domain.exception.shippingorder;

import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.waterside.domain.ReferenceId;
import be.kdg.prog6.waterside.domain.ShippingOrderId;

public class ShippingOrderNotFoundException extends NotFoundException {
    public ShippingOrderNotFoundException(final ShippingOrderId id) {
        super("Shipping Order with ID %s not found".formatted(id.id()));
    }

    public ShippingOrderNotFoundException(final ReferenceId referenceId) {
        super("Shipping Order with Reference ID %s not found".formatted(referenceId.id()));
    }
}
