package be.kdg.prog6.landside.port.out;

import be.kdg.prog6.landside.domain.Appointment;
import be.kdg.prog6.landside.domain.AppointmentId;
import be.kdg.prog6.landside.domain.AppointmentStatus;
import be.kdg.prog6.landside.domain.WarehouseId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Read-only access to Appointment data for query use cases.
 * Do not use for modifications, load the DailySchedule aggregate instead.
 */
public interface AppointmentQueryPort {
    Optional<Appointment> loadById(AppointmentId appointmentId);

    List<Appointment> loadAppointmentsBetween(LocalDateTime from, LocalDateTime to);

    List<Appointment> loadAppointmentsByStatusAndWarehouseId(AppointmentStatus status, WarehouseId warehouseId);
}
