package be.kdg.prog6.warehousing.domain.storage;

import java.math.BigDecimal;

/**
 * Represents the remainder of a delivery that is still stored in a warehouse.
 * <p>
 * This value object is used for reporting warehouse storage, capturing:
 * <ul>
 *   <li>{@link #deliveryId} – the identifier of the delivery</li>
 *   <li>{@link #remainingAmount} – the unshipped amount of the delivery</li>
 *   <li>{@link #daysStored} – how many full days the remainder has been stored
 *       (may be {@code 0} if stored for less than one day)</li>
 * </ul>
 */
public record StoredDeliveryRemainder(
    DeliveryId deliveryId,
    BigDecimal remainingAmount,
    int daysStored
) {
}