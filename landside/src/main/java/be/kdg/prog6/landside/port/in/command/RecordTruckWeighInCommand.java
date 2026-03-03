package be.kdg.prog6.landside.port.in.command;

import be.kdg.prog6.common.exception.InvalidOperationException;
import be.kdg.prog6.landside.domain.TruckLicensePlate;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

public record RecordTruckWeighInCommand(
    TruckLicensePlate truckLicensePlate,
    BigDecimal grossWeight // weigh-in (gross weight with payload inside)
) {
    public RecordTruckWeighInCommand {
        requireNonNull(truckLicensePlate);
        requireNonNull(grossWeight);
        if (grossWeight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("Gross weight must be greater than 0");
        }
    }
}
