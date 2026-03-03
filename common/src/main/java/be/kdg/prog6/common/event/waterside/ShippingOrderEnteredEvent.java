package be.kdg.prog6.common.event.waterside;

import java.util.UUID;

public record ShippingOrderEnteredEvent(
    UUID buyerId,
    UUID shippingOrderId,
    UUID referenceId, // to the PO,
    String vesselNumber // Of the Ship (e.g. VES-001)
) {
}
