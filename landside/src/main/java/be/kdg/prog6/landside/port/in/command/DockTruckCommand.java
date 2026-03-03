package be.kdg.prog6.landside.port.in.command;

import be.kdg.prog6.landside.domain.TruckLicensePlate;

import static java.util.Objects.requireNonNull;

/**
 * Command issued when a truck arrives at the designated warehouse to dock and unload its material.
 * This command is part of the landside context, as truck related operations are handled here.
 */
public record DockTruckCommand(TruckLicensePlate truckLicensePlate) {
    public DockTruckCommand {
        requireNonNull(truckLicensePlate);
    }
}
