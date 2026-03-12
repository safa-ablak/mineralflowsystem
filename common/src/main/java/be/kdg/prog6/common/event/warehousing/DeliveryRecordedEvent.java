package be.kdg.prog6.common.event.warehousing;

import java.math.BigDecimal;
import java.util.UUID;

public record DeliveryRecordedEvent(
    UUID warehouseId, // at which warehouse
    UUID sellerId, // of the seller (just informational)
    String rawMaterial, // which raw material the warehouse is storing (just informational)
    BigDecimal amountDelivered, // (just informational)
    BigDecimal percentageFilled // current warehouse % filled after delivery
) {}
