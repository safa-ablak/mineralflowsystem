package be.kdg.prog6.invoicing.port.in.command;

import be.kdg.prog6.invoicing.domain.CustomerId;

import java.math.BigDecimal;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public record CalculateCommissionFeeCommand(
    CustomerId customerId,
    Map<String, BigDecimal> rawMaterialToAmount
) {
    public CalculateCommissionFeeCommand {
        requireNonNull(customerId);
        requireNonNull(rawMaterialToAmount);
    }
}
