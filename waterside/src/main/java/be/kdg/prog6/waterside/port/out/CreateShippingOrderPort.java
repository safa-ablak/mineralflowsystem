package be.kdg.prog6.waterside.port.out;

import be.kdg.prog6.waterside.domain.ShippingOrder;

@FunctionalInterface
public interface CreateShippingOrderPort {
    void createShippingOrder(ShippingOrder shippingOrder);
}
