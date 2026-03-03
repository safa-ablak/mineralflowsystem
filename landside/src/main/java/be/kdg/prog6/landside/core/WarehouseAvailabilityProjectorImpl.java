package be.kdg.prog6.landside.core;

import be.kdg.prog6.landside.domain.Warehouse;
import be.kdg.prog6.landside.port.in.command.ProjectAvailabilityCommand;
import be.kdg.prog6.landside.port.in.projector.WarehouseAvailabilityProjector;
import be.kdg.prog6.landside.port.out.LoadWarehousePort;
import be.kdg.prog6.landside.port.out.UpdateWarehousePort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static be.kdg.prog6.common.BoundedContext.LANDSIDE;

@Service
public class WarehouseAvailabilityProjectorImpl implements WarehouseAvailabilityProjector {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseAvailabilityProjectorImpl.class);

    private final LoadWarehousePort loadWarehousePort;
    private final UpdateWarehousePort updateWarehousePort;

    public WarehouseAvailabilityProjectorImpl(final LoadWarehousePort loadWarehousePort,
                                              final UpdateWarehousePort updateWarehousePort) {
        this.loadWarehousePort = loadWarehousePort;
        this.updateWarehousePort = updateWarehousePort;
    }

    @Override
    @Transactional
    public void projectAvailability(final ProjectAvailabilityCommand command) {
        final UUID id = command.warehouseId().id();
        final Optional<Warehouse> optionalWarehouse = loadWarehousePort.loadWarehouseById(command.warehouseId());
        if (optionalWarehouse.isEmpty()) {
            LOGGER.warn("Availability cannot be projected -> Warehouse with ID {} does not exist in {}!", id, LANDSIDE);
            return;
        }
        final Warehouse warehouse = optionalWarehouse.get();

        final boolean availabilityBefore = warehouse.isAvailable();
        warehouse.updateAvailability(command.percentageFilled());
        final boolean availabilityAfter = warehouse.isAvailable();

        if (availabilityBefore == availabilityAfter) {
            LOGGER.info("Availability for Warehouse with ID {} remains unchanged as {}", id, availabilityBefore);
        } else {
            LOGGER.info("Availability for Warehouse with ID {} updated from {} to {}", id, availabilityBefore, availabilityAfter);
        }
        updateWarehousePort.updateWarehouse(warehouse);
    }
}