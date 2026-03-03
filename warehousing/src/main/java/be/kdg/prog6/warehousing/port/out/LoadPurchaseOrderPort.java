package be.kdg.prog6.warehousing.port.out;

import be.kdg.prog6.warehousing.domain.BuyerId;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderStatus;

import java.util.List;
import java.util.Optional;

public interface LoadPurchaseOrderPort {
    Optional<PurchaseOrder> loadPurchaseOrderById(PurchaseOrderId id);

    Optional<PurchaseOrder> loadPurchaseOrderByBuyerIdAndIdAndStatus(
        BuyerId buyerId,
        PurchaseOrderId purchaseOrderId,
        PurchaseOrderStatus status
    );

    List<PurchaseOrder> loadPurchaseOrdersBy(PurchaseOrderStatus status, String sellerName);
}
