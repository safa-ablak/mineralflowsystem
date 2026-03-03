package be.kdg.prog6.warehousing.port.in.usecase;

import be.kdg.prog6.warehousing.port.in.command.FulfillPurchaseOrderCommand;

@FunctionalInterface
public interface FulfillPurchaseOrderUseCase {
    void fulfillPurchaseOrder(FulfillPurchaseOrderCommand command);
}
