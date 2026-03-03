package be.kdg.prog6.invoicing.domain;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

/**
 * Value Object:
 * Represents the commission rate applied to total Raw Material costs.
 * <p>
 * As per the project description:
 * > "KdG gets a commission fee of 1% as decided <b>per year contract</b>."
 * <p>
 * This rate is always expressed as a decimal between 0 and 1 (e.g., 0.01 = 1%).
 */
public record YearlyCommissionRate(int year, BigDecimal rate) {
    public YearlyCommissionRate {
        if (year <= 0) {
            throw new IllegalArgumentException("Year must be a positive integer.");
        }
        requireNonNull(rate);
        if (rate.compareTo(BigDecimal.ZERO) < 0 || rate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Yearly commission rate must be between 0 and 1.");
        }
    }

    public static YearlyCommissionRate of(final int year, final BigDecimal rate) {
        return new YearlyCommissionRate(year, rate);
    }

    /**
     * Returns the rate as a percentage (e.g., 0.01 -> 1)
     */
    public BigDecimal asPercentage() {
        return rate.multiply(BigDecimal.valueOf(100));
    }
}
