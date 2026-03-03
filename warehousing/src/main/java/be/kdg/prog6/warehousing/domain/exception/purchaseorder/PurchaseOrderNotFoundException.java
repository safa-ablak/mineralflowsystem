package be.kdg.prog6.warehousing.domain.exception.purchaseorder;

import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;

public class PurchaseOrderNotFoundException extends NotFoundException {
    private PurchaseOrderNotFoundException(String message) {
        super(message);
    }

    public static PurchaseOrderNotFoundException forId(final PurchaseOrderId id) {
        return new PurchaseOrderNotFoundException(
            String.format("Purchase Order with ID %s not found", id.id())
        );
    }
}
