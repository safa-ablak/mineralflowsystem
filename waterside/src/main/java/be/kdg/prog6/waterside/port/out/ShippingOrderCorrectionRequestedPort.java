package be.kdg.prog6.waterside.port.out;

import be.kdg.prog6.waterside.domain.ShippingOrder;

@FunctionalInterface
public interface ShippingOrderCorrectionRequestedPort {
    void shippingOrderCorrectionRequested(ShippingOrder shippingOrder);
}
