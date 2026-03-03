package be.kdg.prog6.warehousing.port.in.usecase.query;


import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;
import be.kdg.prog6.warehousing.port.in.query.GetPurchaseOrdersQuery;

import java.util.List;

@FunctionalInterface
public interface GetPurchaseOrdersUseCase {
    List<PurchaseOrder> getPurchaseOrders(GetPurchaseOrdersQuery query);
}
