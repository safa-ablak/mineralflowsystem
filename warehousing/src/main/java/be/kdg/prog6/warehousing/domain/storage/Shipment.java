package be.kdg.prog6.warehousing.domain.storage;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a Shipment of Raw Materials <b>from</b> a Warehouse.
 * <p>
 * Could be renamed to ShipmentActivity but it is a clarity/brevity trade-off.
 */
public record Shipment(ShipmentId id, LocalDateTime time, BigDecimal amount) {
    @Override
    public String toString() {
        return "Shipment{id=%s, time=%s, amount=%.2f}".formatted(id, time, amount);
    }
}
