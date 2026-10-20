package be.kdg.prog6.warehousing.domain.exception.purchaseorder;

import be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;

public class EmptyPurchaseOrderException extends WarehousingDomainException {
    public EmptyPurchaseOrderException(final PurchaseOrderId id) {
        super(
            String.format("Purchase Order with ID %s has no Order Lines.", id.id())
        );
    }
}
