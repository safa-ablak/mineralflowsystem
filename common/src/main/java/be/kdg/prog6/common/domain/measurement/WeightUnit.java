package be.kdg.prog6.common.domain.measurement;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Unit of measure for raw material weight.
 * Each unit defines how to convert to and from the canonical base unit (TONS).
 */
public enum WeightUnit {
    TONS("t", BigDecimal.ONE),
    KILOTONS("kt", new BigDecimal("1000"));

    private final String unitOfMeasure;
    private final BigDecimal toTonsFactor;

    WeightUnit(final String unitOfMeasure, final BigDecimal toTonsFactor) {
        this.unitOfMeasure = unitOfMeasure;
        this.toTonsFactor = toTonsFactor;
    }

    /**
     * @return Short symbol for the unit (e.g., "t", "kt").
     */
    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    /**
     * Converts the given amount in this unit to tons.
     */
    public BigDecimal toTons(final BigDecimal amount) {
        return amount.multiply(toTonsFactor);
    }

    /**
     * Converts an amount in tons to this unit.
     */
    public BigDecimal fromTons(final BigDecimal tonsAmount) {
        return tonsAmount.divide(toTonsFactor, 6, RoundingMode.HALF_UP);
    }
}
