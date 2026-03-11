package be.kdg.prog6.landside.core;

import be.kdg.prog6.landside.domain.TruckLicensePlate;
import be.kdg.prog6.landside.domain.Visit;
import be.kdg.prog6.landside.domain.WeighBridge;
import be.kdg.prog6.landside.domain.WeighBridgeTransaction;
import be.kdg.prog6.landside.domain.exception.VisitNotFoundException;
import be.kdg.prog6.landside.port.in.command.RecordTruckWeighInCommand;
import be.kdg.prog6.landside.port.in.usecase.RecordTruckWeighInUseCase;
import be.kdg.prog6.landside.port.out.LoadVisitPort;
import be.kdg.prog6.landside.port.out.LoadWeighBridgePort;
import be.kdg.prog6.landside.port.out.UpdateVisitPort;
import be.kdg.prog6.landside.port.out.UpdateWeighBridgePort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RecordTruckWeighInUseCaseImpl implements RecordTruckWeighInUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordTruckWeighInUseCaseImpl.class);

    private final LoadVisitPort loadVisitPort;
    private final UpdateVisitPort updateVisitPort;
    private final LoadWeighBridgePort loadWeighBridgePort;
    private final UpdateWeighBridgePort updateWeighBridgePort;

    public RecordTruckWeighInUseCaseImpl(final LoadVisitPort loadVisitPort,
                                         final UpdateVisitPort updateVisitPort,
                                         final LoadWeighBridgePort loadWeighBridgePort,
                                         final UpdateWeighBridgePort updateWeighBridgePort) {
        this.loadVisitPort = loadVisitPort;
        this.updateVisitPort = updateVisitPort;
        this.loadWeighBridgePort = loadWeighBridgePort;
        this.updateWeighBridgePort = updateWeighBridgePort;
    }

    @Override
    @Transactional
    public WeighBridgeTransaction recordWeighIn(final RecordTruckWeighInCommand command) {
        final TruckLicensePlate plate = command.truckLicensePlate();
        final Visit visit = loadVisitPort.loadActiveVisitByTruckLicensePlate(plate).orElseThrow(
            () -> VisitNotFoundException.activeForTruck(plate)
        );
        // The weigh bridge was already occupied at entrance gate recognition
        final WeighBridge weighBridge = loadWeighBridgePort.loadByOccupiedVisitId(visit.getVisitId()).orElseThrow();

        LOGGER.info("Recording weigh-in for truck {} on weighbridge {}", plate, weighBridge.getNumber().value());
        final WeighBridgeTransaction transaction = visit.recordWeighIn(command.grossWeight());
        // Release the weigh bridge – truck leaves for the warehouse
        weighBridge.release();
        updateWeighBridgePort.updateWeighBridge(weighBridge);
        updateVisitPort.updateVisit(visit);
        return transaction;
    }
}
