package be.kdg.prog6.common.domain.measurement;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

/// From the project description: Raw material is measured in tons<br><br>
/// This & WeightUnit.java could also be moved to the <b>Warehousing Domain</b>, for the current state of the project
/// (since it is only used in the Warehouse.java & PurchaseOrder.java classes).
public record Weight(WeightUnit unit, BigDecimal amount) {
    public Weight {
        requireNonNull(unit, "Weight unit must not be null");
        requireNonNull(amount, "Weight amount must not be null");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Weight amount must be positive");
        }
    }

    /**
     * Converts this weight to tons, regardless of the original unit.
     */
    public BigDecimal toTons() {
        return unit.toTons(amount);
    }

    /**
     * Converts this weight to a new Weight in the specified target unit.
     */
    public Weight toUnit(final WeightUnit targetUnit) {
        return new Weight(targetUnit, targetUnit.fromTons(this.toTons()));
    }

    @Override
    public String toString() {
        return amount.stripTrailingZeros().toPlainString() + " " + unit.getUnitOfMeasure();
    }
}
