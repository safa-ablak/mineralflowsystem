package be.kdg.prog6.warehousing.port.in.query;

import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderStatus;

public record GetPurchaseOrdersQuery(
    PurchaseOrderStatus status,
    String sellerName
) {
}
