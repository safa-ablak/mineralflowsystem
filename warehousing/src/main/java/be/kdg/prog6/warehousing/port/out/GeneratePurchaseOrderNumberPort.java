package be.kdg.prog6.warehousing.port.out;

import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderNumber;

@FunctionalInterface
public interface GeneratePurchaseOrderNumberPort {
    PurchaseOrderNumber generatePurchaseOrderNumber();
}
