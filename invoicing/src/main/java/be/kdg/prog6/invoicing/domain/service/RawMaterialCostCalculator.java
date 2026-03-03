package be.kdg.prog6.invoicing.domain.service;

import be.kdg.prog6.invoicing.domain.Money;
import be.kdg.prog6.invoicing.domain.RawMaterial;

import java.math.BigDecimal;
import java.util.Map;

/**
 * A domain service for calculating total raw material costs.
 */
public class RawMaterialCostCalculator {
    /**
     * Computes the total cost of all raw materials.
     * Note: Raw Material Costs are NOT invoiced to the Customer (Seller)
     *
     * @param rawMaterialToAmount A map of raw materials and their sold amounts.
     * @return Total raw material cost (used for commission calculation).
     */
    public Money calculateTotalRawMaterialCosts(final Map<RawMaterial, BigDecimal> rawMaterialToAmount) {
        Money total = Money.of(0);

        for (Map.Entry<RawMaterial, BigDecimal> entry : rawMaterialToAmount.entrySet()) {
            final RawMaterial rawMaterial = entry.getKey();
            final BigDecimal amount = entry.getValue();

            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                final Money rawMaterialCost = rawMaterial.getUnitPricePerTon().multiply(amount);
                total = total.add(rawMaterialCost);
            }
        }
        return total;
    }
}
