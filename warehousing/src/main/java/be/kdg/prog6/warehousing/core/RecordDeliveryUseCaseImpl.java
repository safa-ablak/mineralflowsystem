package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.domain.exception.storage.WarehouseNotFoundException;
import be.kdg.prog6.warehousing.domain.storage.Delivery;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.port.in.command.RecordDeliveryCommand;
import be.kdg.prog6.warehousing.port.in.usecase.RecordDeliveryUseCase;
import be.kdg.prog6.warehousing.port.out.DeliveryRecordedPort;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordDeliveryUseCaseImpl implements RecordDeliveryUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordDeliveryUseCaseImpl.class);

    private final LoadWarehousePort loadWarehousePort;
    private final List<DeliveryRecordedPort> deliveryRecordedPorts;

    public RecordDeliveryUseCaseImpl(final LoadWarehousePort loadWarehousePort,
                                     final List<DeliveryRecordedPort> deliveryRecordedPorts) {
        this.loadWarehousePort = loadWarehousePort;
        this.deliveryRecordedPorts = deliveryRecordedPorts;
    }

    @Override
    @Transactional
    public void recordDelivery(final RecordDeliveryCommand command) {
        final WarehouseId warehouseId = command.warehouseId();
        final Warehouse warehouse = loadWarehousePort.loadWarehouseById(warehouseId).orElseThrow(
            () -> WarehouseNotFoundException.forId(warehouseId)
        );
        final Delivery delivery = warehouse.recordDelivery(command.deliveryAmount());
        // Update the warehouse in the db + publish event for delivery
        deliveryRecordedPorts.forEach(
            port -> port.deliveryRecorded(warehouse, delivery)
        );
        LOGGER.debug(
            "Recorded delivery for Warehouse with ID {} (Raw Material: {}) -> New Balance: {}",
            warehouseId.id(), warehouse.getRawMaterial().getDisplayName(), warehouse.calculateStockLevel().balance()
        );
    }
}
