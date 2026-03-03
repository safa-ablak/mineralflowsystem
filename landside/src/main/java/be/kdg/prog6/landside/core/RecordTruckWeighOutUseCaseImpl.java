package be.kdg.prog6.landside.core;

import be.kdg.prog6.common.event.landside.TruckWeighedOutEvent;
import be.kdg.prog6.landside.adapter.out.publisher.TruckWeighedOutPublisher;
import be.kdg.prog6.landside.domain.TruckLicensePlate;
import be.kdg.prog6.landside.domain.Visit;
import be.kdg.prog6.landside.domain.WeighBridge;
import be.kdg.prog6.landside.domain.WeighBridgeTransaction;
import be.kdg.prog6.landside.domain.exception.VisitNotFoundException;
import be.kdg.prog6.landside.port.in.command.RecordTruckWeighOutCommand;
import be.kdg.prog6.landside.port.in.usecase.RecordTruckWeighOutUseCase;
import be.kdg.prog6.landside.port.out.LoadVisitPort;
import be.kdg.prog6.landside.port.out.LoadWeighBridgePort;
import be.kdg.prog6.landside.port.out.UpdateVisitPort;
import be.kdg.prog6.landside.port.out.UpdateWeighBridgePort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RecordTruckWeighOutUseCaseImpl implements RecordTruckWeighOutUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordTruckWeighOutUseCaseImpl.class);

    private final LoadVisitPort loadVisitPort;
    private final UpdateVisitPort updateVisitPort;
    private final LoadWeighBridgePort loadWeighBridgePort;
    private final UpdateWeighBridgePort updateWeighBridgePort;
    private final TruckWeighedOutPublisher publisher;

    public RecordTruckWeighOutUseCaseImpl(final LoadVisitPort loadVisitPort,
                                          final UpdateVisitPort updateVisitPort,
                                          final LoadWeighBridgePort loadWeighBridgePort,
                                          final UpdateWeighBridgePort updateWeighBridgePort,
                                          final TruckWeighedOutPublisher publisher) {
        this.loadVisitPort = loadVisitPort;
        this.updateVisitPort = updateVisitPort;
        this.loadWeighBridgePort = loadWeighBridgePort;
        this.updateWeighBridgePort = updateWeighBridgePort;
        this.publisher = publisher;
    }

    @Override
    @Transactional
    public WeighBridgeTransaction recordWeighOut(final RecordTruckWeighOutCommand command) {
        final TruckLicensePlate plate = command.truckLicensePlate();
        final Visit visit = loadVisitPort.loadActiveVisitByTruckLicensePlate(plate).orElseThrow(
            () -> VisitNotFoundException.activeForTruck(plate)
        );
        // The weigh bridge was already occupied at docking
        final WeighBridge weighBridge = loadWeighBridgePort.loadByOccupiedVisitId(visit.getVisitId()).orElseThrow();
        LOGGER.info(
            "Recording weigh-out for truck {} on weighbridge {}", plate, weighBridge.getNumber().value()
        );
        final WeighBridgeTransaction transaction = visit.recordWeighOut(command.tareWeight());
        // Release the weigh bridge – truck leaves for the exit gate
        weighBridge.release();
        updateWeighBridgePort.updateWeighBridge(weighBridge);
        updateVisitPort.updateVisit(visit);
        // Publish event
        final BigDecimal netWeight = transaction.calculateNetWeight();
        publisher.truckWeighedOut(new TruckWeighedOutEvent(visit.getWarehouseId().id(), netWeight));
        return transaction;
    }
}
