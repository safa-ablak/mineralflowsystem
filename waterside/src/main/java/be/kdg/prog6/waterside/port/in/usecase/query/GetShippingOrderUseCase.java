package be.kdg.prog6.waterside.port.in.usecase.query;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.domain.ShippingOrderId;

@FunctionalInterface
public interface GetShippingOrderUseCase {
    ShippingOrder getShippingOrder(ShippingOrderId shippingOrderId);
}
