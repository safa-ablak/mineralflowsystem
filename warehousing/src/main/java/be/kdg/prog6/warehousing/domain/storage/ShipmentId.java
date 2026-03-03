package be.kdg.prog6.warehousing.domain.storage;

import java.util.UUID;

/**
 * Unique identifier for a Shipment entry within a specific Warehouse.
 * Could be renamed to ShipmentActivityId but it is a clarity/brevity trade-off.
 */
public record ShipmentId(
    WarehouseId warehouseId,
    UUID id
) {
    /// For mapping/testing
    public static ShipmentId of(final WarehouseId warehouseId, final UUID id) {
        return new ShipmentId(warehouseId, id);
    }

    /// To create a random ID for a shipment within a specific warehouse
    public static ShipmentId forWarehouse(final WarehouseId warehouseId) {
        return new ShipmentId(warehouseId, UUID.randomUUID());
    }
}
