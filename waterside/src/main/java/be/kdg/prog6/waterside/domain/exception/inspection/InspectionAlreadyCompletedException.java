package be.kdg.prog6.waterside.domain.exception.inspection;

import be.kdg.prog6.waterside.domain.ShippingOrderId;
import be.kdg.prog6.waterside.domain.exception.WatersideDomainException;

public class InspectionAlreadyCompletedException extends WatersideDomainException {
    public InspectionAlreadyCompletedException(final ShippingOrderId shippingOrderId) {
        super(String.format(
            "Inspection Operation for Shipping Order with ID %s is already completed", shippingOrderId.id()
        ));
    }
}
