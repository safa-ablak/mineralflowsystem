package be.kdg.prog6.warehousing.domain.exception.purchaseorder;

import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;

public class PurchaseOrderNotFoundException extends NotFoundException {
    public PurchaseOrderNotFoundException(final PurchaseOrderId id) {
        super(String.format("Purchase Order with ID %s not found", id.id()));
    }
}
