package be.kdg.prog6.landside.port.in.command;

import be.kdg.prog6.landside.domain.TruckLicensePlate;

import static java.util.Objects.requireNonNull;

public record ConsolidateTruckAtExitGateCommand(TruckLicensePlate truckLicensePlate) {
    public ConsolidateTruckAtExitGateCommand {
        requireNonNull(truckLicensePlate);
    }
}
