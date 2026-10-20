package be.kdg.prog6.waterside.domain.exception.bunkering;

import be.kdg.prog6.waterside.domain.ShippingOrderId;
import be.kdg.prog6.waterside.domain.exception.WatersideDomainException;

public class BunkeringAlreadyCompletedException extends WatersideDomainException {
    public BunkeringAlreadyCompletedException(final ShippingOrderId shippingOrderId) {
        super(
            String.format("Bunkering Operation for Shipping Order with ID %s is already completed",
                shippingOrderId.id()
            )
        );
    }
}
