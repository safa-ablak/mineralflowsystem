package be.kdg.prog6.landside.core;

import be.kdg.prog6.common.event.landside.TruckDockedEvent;
import be.kdg.prog6.landside.adapter.out.publisher.TruckDockedPublisher;
import be.kdg.prog6.landside.domain.Dock;
import be.kdg.prog6.landside.domain.TruckLicensePlate;
import be.kdg.prog6.landside.domain.Visit;
import be.kdg.prog6.landside.domain.WeighBridge;
import be.kdg.prog6.landside.domain.exception.NoWeighBridgeAvailableException;
import be.kdg.prog6.landside.domain.exception.VisitNotFoundException;
import be.kdg.prog6.landside.port.in.command.DockTruckCommand;
import be.kdg.prog6.landside.port.in.usecase.DockTruckUseCase;
import be.kdg.prog6.landside.port.in.usecase.model.DockedTruck;
import be.kdg.prog6.landside.port.out.LoadVisitPort;
import be.kdg.prog6.landside.port.out.LoadWeighBridgePort;
import be.kdg.prog6.landside.port.out.UpdateVisitPort;
import be.kdg.prog6.landside.port.out.UpdateWeighBridgePort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DockTruckUseCaseImpl implements DockTruckUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockTruckUseCaseImpl.class);

    private final LoadVisitPort loadVisitPort; // To load the active visit for the truck
    private final UpdateVisitPort updateVisitPort;
    private final LoadWeighBridgePort loadWeighBridgePort;
    private final UpdateWeighBridgePort updateWeighBridgePort;
    private final TruckDockedPublisher publisher;

    public DockTruckUseCaseImpl(final LoadVisitPort loadVisitPort,
                                final UpdateVisitPort updateVisitPort,
                                final LoadWeighBridgePort loadWeighBridgePort,
                                final UpdateWeighBridgePort updateWeighBridgePort,
                                final TruckDockedPublisher publisher) {
        this.loadVisitPort = loadVisitPort;
        this.updateVisitPort = updateVisitPort;
        this.loadWeighBridgePort = loadWeighBridgePort;
        this.updateWeighBridgePort = updateWeighBridgePort;
        this.publisher = publisher;
    }

    @Override
    @Transactional
    public DockedTruck dockTruck(final DockTruckCommand command) {
        final Dock assignedDock = Dock.random();
        final TruckLicensePlate plate = command.truckLicensePlate();
        LOGGER.info("Attempting to dock truck with license plate {}", plate.value());
        final Visit visit = loadVisitPort.loadActiveVisitByTruckLicensePlate(plate).orElseThrow(
            () -> VisitNotFoundException.activeForTruck(plate)
        );
        // Occupy a new weigh bridge for the weigh-out return trip
        final WeighBridge weighBridge = loadWeighBridgePort.loadClosestAvailableWeighBridge().orElseThrow(
            NoWeighBridgeAvailableException::new
        );
        weighBridge.occupy(visit.getVisitId());
        updateWeighBridgePort.updateWeighBridge(weighBridge);
        LOGGER.info("Truck {} docked at dock {}, assigned weighbridge {} for weigh-out",
            plate.value(), assignedDock.number(), weighBridge.getNumber().value()
        );
        visit.dockTruck();
        updateVisitPort.updateVisit(visit);
        publisher.truckDocked(
            new TruckDockedEvent(visit.getWarehouseId().id(), assignedDock.number(), visit.getRawMaterial().name())
        );
        return new DockedTruck(plate, assignedDock.number(), weighBridge.getNumber().value());
    }
}
