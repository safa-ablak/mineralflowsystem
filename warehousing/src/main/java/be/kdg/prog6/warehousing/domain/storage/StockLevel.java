package be.kdg.prog6.warehousing.domain.storage;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.util.Objects.requireNonNull;

/**
 * Immutable Value Object representing a warehouse's stock level at a point in time.
 * Encapsulates both the balance and the derived percentage filled,
 * ensuring the percentage is calculated exactly once.
 */
public record StockLevel(
    Balance balance,
    BigDecimal percentageFilled
) {
    private static final int PERCENTAGE_SCALE = 4;

    public StockLevel {
        requireNonNull(balance, "Balance must not be null");
        requireNonNull(percentageFilled, "Percentage filled must not be null");
    }

    /**
     * Factory method that calculates percentage from balance and capacity.
     *
     * @param balance  the current balance (time and amount)
     * @param capacity the warehouse regular capacity (not overflow capacity)
     */
    static StockLevel from(final Balance balance, final BigDecimal capacity) {
        if (capacity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        final BigDecimal percentageFilled = balance.amount()
            .multiply(BigDecimal.valueOf(100))
            .divide(capacity, PERCENTAGE_SCALE, RoundingMode.HALF_UP);
        return new StockLevel(balance, percentageFilled);
    }
}
