package be.kdg.prog6.warehousing.domain.storage;

import java.util.List;

/**
 * Represents a Shipment together with the Delivery allocations it was drawn from.
 * <p>
 * Immutable value object linking a Shipment to the Deliveries it was allocated from,
 * as recorded in the {@link StockLedger}.
 */
public record ShipmentRecord(
    Shipment shipment,
    List<ShipmentAllocation> allocations
) {
}
