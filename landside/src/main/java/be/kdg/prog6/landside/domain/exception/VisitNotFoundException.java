package be.kdg.prog6.landside.domain.exception;

import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.landside.domain.TruckLicensePlate;

public class VisitNotFoundException extends NotFoundException {
    public VisitNotFoundException(final TruckLicensePlate plate) {
        super(
            String.format("No active Visit found for Truck %s", plate)
        );
    }
}
