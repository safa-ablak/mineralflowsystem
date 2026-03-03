package be.kdg.prog6.warehousing.domain.storage;

import java.util.UUID;

/**
 * Unique identifier for a Delivery entry within a specific Warehouse.
 * <p>
 * Could be renamed to DeliveryActivityId but it is a clarity/brevity trade-off.
 */
public record DeliveryId(
    WarehouseId warehouseId,
    UUID id
) {
    /// For mapping/testing
    public static DeliveryId of(final WarehouseId warehouseId, final UUID id) {
        return new DeliveryId(warehouseId, id);
    }

    /// To create a random ID for a delivery within a specific warehouse/testing
    public static DeliveryId forWarehouse(final WarehouseId warehouseId) {
        return new DeliveryId(warehouseId, UUID.randomUUID());
    }
}
