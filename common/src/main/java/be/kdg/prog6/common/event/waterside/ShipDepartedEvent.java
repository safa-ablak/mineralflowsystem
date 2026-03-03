package be.kdg.prog6.common.event.waterside;

import java.util.UUID;

public record ShipDepartedEvent(
    UUID shippingOrderId, // May not be used, but doesn't hurt to have it.
    UUID referenceId
) {
}
