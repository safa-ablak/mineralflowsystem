package be.kdg.prog6.landside.core;

import be.kdg.prog6.landside.domain.*;
import be.kdg.prog6.landside.domain.exception.DailyScheduleNotAvailableException;
import be.kdg.prog6.landside.domain.exception.NoWeighBridgeAvailableException;
import be.kdg.prog6.landside.domain.exception.TruckNotRecognizedException;
import be.kdg.prog6.landside.port.in.command.RecognizeTruckAtEntranceGateCommand;
import be.kdg.prog6.landside.port.in.usecase.RecognizeTruckAtEntranceGateUseCase;
import be.kdg.prog6.landside.port.out.LoadDailySchedulePort;
import be.kdg.prog6.landside.port.out.LoadWeighBridgePort;
import be.kdg.prog6.landside.port.out.UpdateVisitPort;
import be.kdg.prog6.landside.port.out.UpdateWeighBridgePort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class RecognizeTruckAtEntranceGateUseCaseImpl implements RecognizeTruckAtEntranceGateUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecognizeTruckAtEntranceGateUseCaseImpl.class);

    private final LoadDailySchedulePort loadDailySchedulePort;
    private final UpdateVisitPort updateVisitPort;
    private final LoadWeighBridgePort loadWeighBridgePort;
    private final UpdateWeighBridgePort updateWeighBridgePort;

    public RecognizeTruckAtEntranceGateUseCaseImpl(final LoadDailySchedulePort loadDailySchedulePort,
                                                   final UpdateVisitPort updateVisitPort,
                                                   final LoadWeighBridgePort loadWeighBridgePort,
                                                   final UpdateWeighBridgePort updateWeighBridgePort) {
        this.loadDailySchedulePort = loadDailySchedulePort;
        this.updateVisitPort = updateVisitPort;
        this.loadWeighBridgePort = loadWeighBridgePort;
        this.updateWeighBridgePort = updateWeighBridgePort;
    }

    @Override
    @Transactional
    public String recognizeTruckAndAssignWeighBridge(final RecognizeTruckAtEntranceGateCommand command) {
        final TruckLicensePlate licensePlate = command.truckLicensePlate();
        final LocalDateTime arrivalTime = LocalDateTime.now();
        final LocalDate date = arrivalTime.toLocalDate();

        final DailySchedule dailySchedule = loadDailySchedulePort
            .loadDailyScheduleByDate(date).orElseThrow(
                () -> DailyScheduleNotAvailableException.forDate(date)
            );
        final Appointment appointment = dailySchedule
            .findAppointmentByTruckAndTime(licensePlate, arrivalTime).orElseThrow(
                () -> TruckNotRecognizedException.forLicensePlate(licensePlate)
            );
        // If an appointment is found within that time slot, check if it's an early/late arrival
        // PS: Need to look into the definition of arrival window, if `appointment.getArrivalWindowEnd()` is later
        // than the `endTime` of the TimeSlot in belongs to then it makes no sense to do something like this.
        appointment.ensureWithinArrivalWindow(arrivalTime);

        // Ensure a weighbridge is available before admitting the truck
        final WeighBridge assignedWeighBridge =
            loadWeighBridgePort.loadClosestAvailableWeighBridge().orElseThrow(NoWeighBridgeAvailableException::new);

        // Marks the start of the truck's visit.
        final Visit visit =
            new Visit(appointment.getAppointmentId(), appointment.getWarehouseId(), licensePlate, appointment.getRawMaterial(), arrivalTime);
        updateVisitPort.updateVisit(visit);

        assignedWeighBridge.occupy(visit.getVisitId());
        updateWeighBridgePort.updateWeighBridge(assignedWeighBridge);

        LOGGER.info("Truck with License Plate: {} has been recognized and assigned Weigh Bridge: {}",
            licensePlate, assignedWeighBridge.getNumber().value()
        );
        return assignedWeighBridge.getNumber().value();
    }
}
