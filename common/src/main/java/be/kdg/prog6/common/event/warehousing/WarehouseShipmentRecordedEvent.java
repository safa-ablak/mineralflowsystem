package be.kdg.prog6.common.event.warehousing;

import java.math.BigDecimal;
import java.util.UUID;

public record WarehouseShipmentRecordedEvent(
    UUID warehouseId, // at which warehouse
    UUID sellerId, // of the seller (just informational)
    String rawMaterial, // which raw material the warehouse is storing (just informational)
    BigDecimal amountShipped, // (just informational)
    BigDecimal percentageFilled // current warehouse % filled after shipment
) {}