package be.kdg.prog6.landside.port.in.command;

import be.kdg.prog6.landside.domain.TruckLicensePlate;

import static java.util.Objects.requireNonNull;

/*
 * Description: Command issued when a truck arrives at the entrance gate
 *  and its license plate needs to be recognized.
 * */
public record RecognizeTruckAtEntranceGateCommand(TruckLicensePlate truckLicensePlate) {
    public RecognizeTruckAtEntranceGateCommand {
        requireNonNull(truckLicensePlate);
    }
}
