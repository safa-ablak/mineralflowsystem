package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.domain.exception.storage.WarehouseNotFoundException;
import be.kdg.prog6.warehousing.domain.storage.RawMaterial;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.port.in.command.AssignRawMaterialToWarehouseCommand;
import be.kdg.prog6.warehousing.port.in.usecase.AssignRawMaterialToWarehouseUseCase;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import be.kdg.prog6.warehousing.port.out.RawMaterialAssignedPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignRawMaterialToWarehouseUseCaseImpl implements AssignRawMaterialToWarehouseUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssignRawMaterialToWarehouseUseCaseImpl.class);

    private final LoadWarehousePort loadWarehousePort;
    private final List<RawMaterialAssignedPort> rawMaterialAssignedPorts;

    public AssignRawMaterialToWarehouseUseCaseImpl(final LoadWarehousePort loadWarehousePort,
                                                   final List<RawMaterialAssignedPort> rawMaterialAssignedPorts) {
        this.loadWarehousePort = loadWarehousePort;
        this.rawMaterialAssignedPorts = rawMaterialAssignedPorts;
    }

    @Override
    @Transactional
    public void assignRawMaterial(final AssignRawMaterialToWarehouseCommand command) {
        final RawMaterial rawMaterial = command.rawMaterial();
        final WarehouseId warehouseId = command.warehouseId();
        final Warehouse warehouse = loadWarehousePort.loadWarehouseById(warehouseId).orElseThrow(
            () -> WarehouseNotFoundException.forId(warehouseId)
        );
        warehouse.assignRawMaterial(rawMaterial);
        rawMaterialAssignedPorts.forEach(port -> port.rawMaterialAssigned(warehouse));
        LOGGER.info("Assigned Raw Material {} to Warehouse {}", rawMaterial, warehouseId.id());
    }
}
