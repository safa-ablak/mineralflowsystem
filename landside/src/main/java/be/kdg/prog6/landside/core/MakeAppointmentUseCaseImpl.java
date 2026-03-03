package be.kdg.prog6.landside.core;

import be.kdg.prog6.landside.domain.*;
import be.kdg.prog6.landside.domain.exception.DailyScheduleNotAvailableException;
import be.kdg.prog6.landside.domain.exception.WarehouseNotFoundException;
import be.kdg.prog6.landside.port.in.command.MakeAppointmentCommand;
import be.kdg.prog6.landside.port.in.usecase.MakeAppointmentUseCase;
import be.kdg.prog6.landside.port.out.LoadDailySchedulePort;
import be.kdg.prog6.landside.port.out.LoadWarehousePort;
import be.kdg.prog6.landside.port.out.UpdateDailySchedulePort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class MakeAppointmentUseCaseImpl implements MakeAppointmentUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MakeAppointmentUseCaseImpl.class);

    private final LoadDailySchedulePort loadDailySchedulePort;
    private final LoadWarehousePort loadWarehousePort;
    private final UpdateDailySchedulePort updateDailySchedulePort;
    private final Clock clock;

    public MakeAppointmentUseCaseImpl(final LoadDailySchedulePort loadDailySchedulePort,
                                      final LoadWarehousePort loadWarehousePort,
                                      final UpdateDailySchedulePort updateDailySchedulePort,
                                      final Clock clock) {
        this.loadDailySchedulePort = loadDailySchedulePort;
        this.loadWarehousePort = loadWarehousePort;
        this.updateDailySchedulePort = updateDailySchedulePort;
        this.clock = clock;
    }

    @Override
    @Transactional
    public Appointment makeAppointment(final MakeAppointmentCommand command) {
        final SupplierId supplierId = command.supplierId();
        final TruckLicensePlate licensePlate = command.truckLicensePlate();
        final RawMaterial rawMaterial = command.rawMaterial();
        final LocalDateTime scheduledArrivalTime = command.scheduledArrivalTime();
        final LocalDate date = scheduledArrivalTime.toLocalDate();

        final DailySchedule schedule = loadDailySchedulePort
            .loadDailyScheduleByDate(date).orElseThrow(
                () -> DailyScheduleNotAvailableException.forDate(date)
            );
        // Note: We assume that a supplier can only have at most one warehouse per raw material type.
        final Warehouse warehouse = loadWarehousePort
            .loadWarehouseBySupplierIdAndRawMaterial(supplierId, rawMaterial).orElseThrow(
                () -> WarehouseNotFoundException.forSupplierIdAndRawMaterial(supplierId, rawMaterial)
            );
        // Availability for Making Appointments is business logic -> So it should be checked in the domain layer after
        // loading the Warehouse from the DB.
        warehouse.ensureAvailableForNewAppointment(); // Ensure the warehouse is available for a new Appointment

        final Appointment appointment =
            schedule.makeAppointment(supplierId, licensePlate, rawMaterial, scheduledArrivalTime, warehouse.getWarehouseId(), LocalDateTime.now(clock));
        updateDailySchedulePort.updateDailySchedule(schedule);
        LOGGER.info("Made an Appointment for Supplier with ID {}, for Truck with License Plate {}, Raw Material {} for {}",
            supplierId.id(), licensePlate.value(), rawMaterial, scheduledArrivalTime
        );
        return appointment;
    }
}
