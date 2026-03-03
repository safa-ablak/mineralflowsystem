package be.kdg.prog6.waterside.port.in.usecase.query;

import be.kdg.prog6.waterside.domain.ShippingOrder;

import java.util.List;

@FunctionalInterface
public interface GetShippingOrdersUseCase {
    List<ShippingOrder> getShippingOrders();
}
