package be.kdg.prog6.landside.domain.exception;

import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.landside.domain.TruckLicensePlate;

public class VisitNotFoundException extends NotFoundException {
    private VisitNotFoundException(String message) {
        super(message);
    }

    public static VisitNotFoundException activeForTruck(final TruckLicensePlate plate) {
        return new VisitNotFoundException(
            String.format("No active Visit found for Truck %s", plate)
        );
    }
}
