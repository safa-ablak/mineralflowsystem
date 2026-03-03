package be.kdg.prog6.waterside.port.in.usecase;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.port.in.command.EnterShippingOrderCommand;

@FunctionalInterface
public interface EnterShippingOrderUseCase {
    ShippingOrder enterShippingOrder(EnterShippingOrderCommand command);
}
