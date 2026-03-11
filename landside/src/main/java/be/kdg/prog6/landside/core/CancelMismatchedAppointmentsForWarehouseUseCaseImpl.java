package be.kdg.prog6.landside.core;

import be.kdg.prog6.landside.domain.*;
import be.kdg.prog6.landside.port.in.command.CancelMismatchedAppointmentsForWarehouseCommand;
import be.kdg.prog6.landside.port.in.usecase.CancelMismatchedAppointmentsForWarehouseUseCase;
import be.kdg.prog6.landside.port.out.AppointmentQueryPort;
import be.kdg.prog6.landside.port.out.LoadDailySchedulePort;
import be.kdg.prog6.landside.port.out.UpdateDailySchedulePort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CancelMismatchedAppointmentsForWarehouseUseCaseImpl implements CancelMismatchedAppointmentsForWarehouseUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CancelMismatchedAppointmentsForWarehouseUseCaseImpl.class);

    private final AppointmentQueryPort appointmentQueryPort;
    private final LoadDailySchedulePort loadDailySchedulePort;
    private final UpdateDailySchedulePort updateDailySchedulePort;

    public CancelMismatchedAppointmentsForWarehouseUseCaseImpl(final AppointmentQueryPort appointmentQueryPort,
                                                               final LoadDailySchedulePort loadDailySchedulePort,
                                                               final UpdateDailySchedulePort updateDailySchedulePort) {
        this.appointmentQueryPort = appointmentQueryPort;
        this.loadDailySchedulePort = loadDailySchedulePort;
        this.updateDailySchedulePort = updateDailySchedulePort;
    }

    @Override
    @Transactional
    public void cancelAppointmentsWithMismatchedRawMaterial(final CancelMismatchedAppointmentsForWarehouseCommand command) {
        final WarehouseId warehouseId = command.warehouseId();
        final RawMaterial newRawMaterial = command.newRawMaterial();
        final List<Appointment> scheduledAppointments =
            appointmentQueryPort.loadAppointmentsByStatusAndWarehouseId(AppointmentStatus.SCHEDULED, warehouseId);
        final List<Appointment> mismatchedAppointments = scheduledAppointments.stream()
            .filter(appointment -> appointment.getRawMaterial() != newRawMaterial)
            .toList();
        if (mismatchedAppointments.isEmpty()) {
            LOGGER.debug("No mismatched Appointments to cancel for Warehouse {}", warehouseId.id());
            return;
        }
        LOGGER.info("Cancelling {} mismatched Appointment(s) for Warehouse {} (new Raw Material: {})",
            mismatchedAppointments.size(), warehouseId.id(), newRawMaterial
        );
        final Map<LocalDate, List<Appointment>> byDate =
            mismatchedAppointments.stream().collect(Collectors.groupingBy(a -> a.getArrivalWindowStart().toLocalDate()));
        for (var entry : byDate.entrySet()) {
            final DailySchedule dailySchedule = loadDailySchedulePort.loadDailyScheduleByDate(entry.getKey()).orElseThrow();
            for (final Appointment appointment : entry.getValue()) {
                dailySchedule.cancelAppointment(appointment.getAppointmentId());
                LOGGER.debug(
                    "Cancelled Appointment {} for Warehouse {} (was {}, now {})",
                    appointment.getAppointmentId().id(), warehouseId.id(),
                    appointment.getRawMaterial(), newRawMaterial
                );
            }
            updateDailySchedulePort.updateDailySchedule(dailySchedule);
        }
    }
}
