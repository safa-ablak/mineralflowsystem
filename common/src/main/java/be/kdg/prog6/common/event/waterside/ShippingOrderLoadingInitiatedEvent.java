package be.kdg.prog6.common.event.waterside;

import java.util.UUID;

public record ShippingOrderLoadingInitiatedEvent(
    UUID shippingOrderId,
    UUID referenceId
) {}
