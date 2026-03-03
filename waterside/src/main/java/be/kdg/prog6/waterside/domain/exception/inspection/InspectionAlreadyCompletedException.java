package be.kdg.prog6.waterside.domain.exception.inspection;

import be.kdg.prog6.waterside.domain.ShippingOrderId;
import be.kdg.prog6.waterside.domain.exception.WatersideDomainException;

public class InspectionAlreadyCompletedException extends WatersideDomainException {
    private InspectionAlreadyCompletedException(final String message) {
        super(message);
    }

    public static InspectionAlreadyCompletedException forShippingOrder(final ShippingOrderId shippingOrderId) {
        return new InspectionAlreadyCompletedException(
            String.format("Inspection Operation for Shipping Order with ID %s is already completed",
                shippingOrderId.id()
            )
        );
    }
}
