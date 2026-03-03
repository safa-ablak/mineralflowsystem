package be.kdg.prog6.warehousing.domain.exception.purchaseorder;

import be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;

public class EmptyPurchaseOrderException extends WarehousingDomainException {
    private EmptyPurchaseOrderException(final String message) {
        super(message);
    }

    public static EmptyPurchaseOrderException forId(final PurchaseOrderId id) {
        return new EmptyPurchaseOrderException(
            String.format("Purchase Order with ID %s has no Order Lines.", id.id())
        );
    }
}
