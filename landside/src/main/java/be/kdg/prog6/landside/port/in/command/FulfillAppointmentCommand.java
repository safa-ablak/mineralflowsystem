package be.kdg.prog6.landside.port.in.command;


import be.kdg.prog6.landside.domain.AppointmentId;

import static java.util.Objects.requireNonNull;

public record FulfillAppointmentCommand(AppointmentId appointmentId) {
    public FulfillAppointmentCommand {
        requireNonNull(appointmentId);
    }
}
