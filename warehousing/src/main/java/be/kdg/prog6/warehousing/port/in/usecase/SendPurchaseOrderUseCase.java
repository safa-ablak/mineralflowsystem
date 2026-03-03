package be.kdg.prog6.warehousing.port.in.usecase;

import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;
import be.kdg.prog6.warehousing.port.in.command.SendPurchaseOrderCommand;

@FunctionalInterface
public interface SendPurchaseOrderUseCase {
    PurchaseOrder sendPurchaseOrder(SendPurchaseOrderCommand command);
}