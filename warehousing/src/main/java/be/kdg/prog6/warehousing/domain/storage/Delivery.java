package be.kdg.prog6.warehousing.domain.storage;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a Delivery of Raw Materials <b>to</b> a Warehouse.
 * <p>
 * Could be renamed to DeliveryActivity but it is a clarity/brevity trade-off.
 */
public record Delivery(DeliveryId id, LocalDateTime time, BigDecimal amount) {
    @Override
    public String toString() {
        return "Delivery{id=%s, time=%s, amount=%.2f}".formatted(id, time, amount);
    }
}
