package be.kdg.prog6.common.event.waterside;

import java.util.UUID;

public record ShippingOrderCorrectionRequestedEvent(
    UUID buyerId,
    UUID shippingOrderId,
    UUID referenceId, // to the PO
    String vesselNumber // unchanged – propagated from the existing SO, not part of the correction
) {}