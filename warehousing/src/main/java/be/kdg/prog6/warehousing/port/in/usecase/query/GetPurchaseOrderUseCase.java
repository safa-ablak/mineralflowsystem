package be.kdg.prog6.warehousing.port.in.usecase.query;

import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;

@FunctionalInterface
public interface GetPurchaseOrderUseCase {
    PurchaseOrder getPurchaseOrder(PurchaseOrderId purchaseOrderId);
}
