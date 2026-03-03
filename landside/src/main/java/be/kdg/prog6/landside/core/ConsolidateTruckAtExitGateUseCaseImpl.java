package be.kdg.prog6.landside.core;

import be.kdg.prog6.landside.adapter.out.publisher.TruckDepartedPublisher;
import be.kdg.prog6.landside.domain.TruckLicensePlate;
import be.kdg.prog6.landside.domain.Visit;
import be.kdg.prog6.landside.domain.event.TruckDepartedEvent;
import be.kdg.prog6.landside.domain.exception.VisitNotFoundException;
import be.kdg.prog6.landside.port.in.command.ConsolidateTruckAtExitGateCommand;
import be.kdg.prog6.landside.port.in.usecase.ConsolidateTruckAtExitGateUseCase;
import be.kdg.prog6.landside.port.out.LoadVisitPort;
import be.kdg.prog6.landside.port.out.UpdateVisitPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConsolidateTruckAtExitGateUseCaseImpl implements ConsolidateTruckAtExitGateUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsolidateTruckAtExitGateUseCaseImpl.class);

    private final LoadVisitPort loadVisitPort;
    private final UpdateVisitPort updateVisitPort;
    private final TruckDepartedPublisher publisher;

    public ConsolidateTruckAtExitGateUseCaseImpl(final LoadVisitPort loadVisitPort,
                                                 final UpdateVisitPort updateVisitPort,
                                                 final TruckDepartedPublisher publisher) {
        this.loadVisitPort = loadVisitPort;
        this.updateVisitPort = updateVisitPort;
        this.publisher = publisher;
    }

    @Override
    @Transactional
    public Visit consolidateTruck(final ConsolidateTruckAtExitGateCommand command) {
        final TruckLicensePlate plate = command.truckLicensePlate();
        final Visit visit = loadVisitPort.loadActiveVisitByTruckLicensePlate(plate).orElseThrow(
            () -> VisitNotFoundException.activeForTruck(plate)
        );
        // Weigh bridge was already released at weigh-out
        final TruckDepartedEvent event = visit.complete();
        updateVisitPort.updateVisit(visit);

        publisher.truckDeparted(event);
        LOGGER.info("Truck with License Plate {} has been recognized and Visit consolidated", plate);

        return visit;
    }
}
