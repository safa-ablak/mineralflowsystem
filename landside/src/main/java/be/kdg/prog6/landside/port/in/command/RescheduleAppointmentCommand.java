package be.kdg.prog6.landside.port.in.command;

import be.kdg.prog6.landside.domain.AppointmentId;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

/* TODO: To be added later */
public record RescheduleAppointmentCommand(
    AppointmentId appointmentId,
    LocalDateTime newScheduledArrivalTime
) {
    public RescheduleAppointmentCommand {
        requireNonNull(appointmentId);
        requireNonNull(newScheduledArrivalTime);
    }
}
