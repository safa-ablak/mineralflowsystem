package be.kdg.prog6.warehousing.domain.exception.purchaseorder;

import be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;

public class PurchaseOrderAlreadyFulfilledException extends WarehousingDomainException {
    private PurchaseOrderAlreadyFulfilledException(final String message) {
        super(message);
    }

    public static PurchaseOrderAlreadyFulfilledException forId(final PurchaseOrderId id) {
        return new PurchaseOrderAlreadyFulfilledException(
            String.format("Purchase order with ID %s is already fulfilled", id.id())
        );
    }
}
