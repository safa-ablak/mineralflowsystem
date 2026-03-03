package be.kdg.prog6.warehousing.port.out;

import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;

@FunctionalInterface
public interface UpdatePurchaseOrderPort {
    void updatePurchaseOrder(PurchaseOrder purchaseOrder);
}