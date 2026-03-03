package be.kdg.prog6.waterside.port.in.usecase;

import be.kdg.prog6.waterside.port.in.command.MatchShippingOrderWithPurchaseOrderCommand;

@FunctionalInterface
public interface MatchShippingOrderWithPurchaseOrderUseCase {
    void matchShippingOrderWithPurchaseOrder(MatchShippingOrderWithPurchaseOrderCommand command);
}
