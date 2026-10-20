package be.kdg.prog6.warehousing.domain.exception.purchaseorder;

import be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;

public class PurchaseOrderAlreadyFulfilledException extends WarehousingDomainException {
    public PurchaseOrderAlreadyFulfilledException(final PurchaseOrderId id) {
        super(
            String.format("Purchase order with ID %s is already fulfilled", id.id())
        );
    }
}
