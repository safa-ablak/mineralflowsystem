package be.kdg.prog6.warehousing.port.in.usecase;

import be.kdg.prog6.warehousing.port.in.command.ShipPurchaseOrderCommand;

@FunctionalInterface
public interface ShipPurchaseOrderUseCase {
    void shipPurchaseOrder(ShipPurchaseOrderCommand command);
}
