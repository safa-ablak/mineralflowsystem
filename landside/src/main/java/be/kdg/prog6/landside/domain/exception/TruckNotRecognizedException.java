package be.kdg.prog6.landside.domain.exception;

import be.kdg.prog6.landside.domain.TruckLicensePlate;

public class TruckNotRecognizedException extends LandsideDomainException {
    public TruckNotRecognizedException(final TruckLicensePlate licensePlate) {
        super("Truck with license plate '%s' not recognized, please check your appointment".formatted(licensePlate));
    }
}
