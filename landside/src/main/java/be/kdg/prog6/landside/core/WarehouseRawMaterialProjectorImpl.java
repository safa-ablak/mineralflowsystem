package be.kdg.prog6.landside.core;

import be.kdg.prog6.landside.domain.RawMaterial;
import be.kdg.prog6.landside.domain.Warehouse;
import be.kdg.prog6.landside.port.in.command.ProjectRawMaterialCommand;
import be.kdg.prog6.landside.port.in.projector.WarehouseRawMaterialProjector;
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
public class WarehouseRawMaterialProjectorImpl implements WarehouseRawMaterialProjector {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseRawMaterialProjectorImpl.class);

    private final LoadWarehousePort loadWarehousePort;
    private final UpdateWarehousePort updateWarehousePort;

    public WarehouseRawMaterialProjectorImpl(final LoadWarehousePort loadWarehousePort,
                                              final UpdateWarehousePort updateWarehousePort) {
        this.loadWarehousePort = loadWarehousePort;
        this.updateWarehousePort = updateWarehousePort;
    }

    @Override
    @Transactional
    public void projectRawMaterial(final ProjectRawMaterialCommand command) {
        final UUID id = command.warehouseId().id();
        final Optional<Warehouse> optionalWarehouse = loadWarehousePort.loadWarehouseById(command.warehouseId());
        if (optionalWarehouse.isEmpty()) {
            LOGGER.warn("Raw material cannot be projected -> Warehouse with ID {} does not exist in {}!", id, LANDSIDE);
            return;
        }
        final Warehouse warehouse = optionalWarehouse.get();
        final RawMaterial oldRawMaterial = warehouse.getRawMaterial();
        warehouse.setRawMaterial(command.rawMaterial());
        final RawMaterial newRawMaterial = warehouse.getRawMaterial();
        updateWarehousePort.updateWarehouse(warehouse);
        LOGGER.info("Updated Raw Material for Warehouse {} from {} to {}", id, oldRawMaterial, newRawMaterial);
    }
}
