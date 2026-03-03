package be.kdg.prog6.warehousing.domain.storage;

import java.math.BigDecimal;

/**
 * Represents how much of a particular Delivery was used for a Shipment in a Warehouse.
 * <p>
 * This is an immutable value object that links a Shipment to a Delivery and the amount shipped from it.
 */
public record ShipmentAllocation(
    WarehouseId warehouseId,
    ShipmentId shipmentId,
    DeliveryId deliveryId,
    BigDecimal amountAllocated
) {
    @Override
    public String toString() {
        return "ShipmentAllocation{shipmentId=%s, deliveryId=%s, amount=%.2f}"
            .formatted(shipmentId.id(), deliveryId.id(), amountAllocated);
    }
}
