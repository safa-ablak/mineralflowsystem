package be.kdg.prog6.landside.domain.exception;

import be.kdg.prog6.landside.domain.TruckLicensePlate;

public class TruckNotRecognizedException extends LandsideDomainException {
    private TruckNotRecognizedException(final String message) {
        super(message);
    }

    public static TruckNotRecognizedException forLicensePlate(final TruckLicensePlate licensePlate) {
        return new TruckNotRecognizedException(
            "Truck with license plate '%s' not recognized, please check your appointment"
                .formatted(licensePlate)
        );
    }
}
