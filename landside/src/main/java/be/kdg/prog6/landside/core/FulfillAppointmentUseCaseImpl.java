package be.kdg.prog6.landside.core;

import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.landside.domain.Appointment;
import be.kdg.prog6.landside.domain.AppointmentId;
import be.kdg.prog6.landside.domain.DailySchedule;
import be.kdg.prog6.landside.port.in.command.FulfillAppointmentCommand;
import be.kdg.prog6.landside.port.in.usecase.FulfillAppointmentUseCase;
import be.kdg.prog6.landside.port.out.LoadDailySchedulePort;
import be.kdg.prog6.landside.port.out.UpdateDailySchedulePort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FulfillAppointmentUseCaseImpl implements FulfillAppointmentUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(FulfillAppointmentUseCaseImpl.class);

    private final LoadDailySchedulePort loadDailySchedulePort;
    private final UpdateDailySchedulePort updateDailySchedulePort;

    public FulfillAppointmentUseCaseImpl(final LoadDailySchedulePort loadDailySchedulePort,
                                         final UpdateDailySchedulePort updateDailySchedulePort) {
        this.loadDailySchedulePort = loadDailySchedulePort;
        this.updateDailySchedulePort = updateDailySchedulePort;
    }

    @Override
    @Transactional
    public void fulfillAppointment(final FulfillAppointmentCommand command) {
        final AppointmentId id = command.appointmentId();
        LOGGER.info("Fulfilling Appointment with ID {}", id.id());

        final DailySchedule dailySchedule = loadDailySchedulePort
            .loadDailyScheduleForAppointmentId(id).orElseThrow(
                () -> new NotFoundException("No Daily Schedule found for Appointment ID %s".formatted(id.id()))
            );
        // Fulfill the appointment via Daily Schedule
        final Appointment appointment = dailySchedule.fulfillAppointment(id);

        updateDailySchedulePort.updateDailySchedule(dailySchedule);
        LOGGER.info("Fulfilled Appointment:\n{}", appointment);
    }
}
