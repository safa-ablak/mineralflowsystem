package be.kdg.prog6.landside.domain.exception;

import be.kdg.prog6.landside.domain.AppointmentId;

public class AppointmentNotFoundException extends RuntimeException {
    private AppointmentNotFoundException(String message) {
        super(message);
    }

    public static AppointmentNotFoundException forId(final AppointmentId id) {
        return new AppointmentNotFoundException(
            String.format("Appointment with ID %s not found", id.id())
        );
    }
}
