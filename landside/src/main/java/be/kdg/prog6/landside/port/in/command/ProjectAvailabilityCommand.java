package be.kdg.prog6.landside.port.in.command;

import be.kdg.prog6.common.exception.InvalidOperationException;
import be.kdg.prog6.landside.domain.WarehouseId;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

public record ProjectAvailabilityCommand(WarehouseId warehouseId, BigDecimal percentageFilled) {
    public ProjectAvailabilityCommand {
        requireNonNull(warehouseId);
        requireNonNull(percentageFilled);
        if (percentageFilled.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOperationException("Percentage filled cannot be negative");
        }
    }
}