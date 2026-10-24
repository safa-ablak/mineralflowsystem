package be.kdg.prog6.landside.domain.exception;

import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.landside.domain.AppointmentId;

public class AppointmentNotFoundException extends NotFoundException {
    public AppointmentNotFoundException(final AppointmentId id) {
        super(String.format("Appointment with ID %s not found", id.id()));
    }
}
