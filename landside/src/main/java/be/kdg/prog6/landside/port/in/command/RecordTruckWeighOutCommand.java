package be.kdg.prog6.landside.port.in.command;

import be.kdg.prog6.common.exception.InvalidOperationException;
import be.kdg.prog6.landside.domain.TruckLicensePlate;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

public record RecordTruckWeighOutCommand(
    TruckLicensePlate truckLicensePlate,
    BigDecimal tareWeight
) {
    public RecordTruckWeighOutCommand {
        requireNonNull(truckLicensePlate);
        requireNonNull(tareWeight);
        if (tareWeight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("Tare weight must be greater than 0");
        }
    }
}
