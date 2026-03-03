package be.kdg.prog6.landside.port.in.command;

import be.kdg.prog6.landside.domain.RawMaterial;
import be.kdg.prog6.landside.domain.WarehouseId;

import static java.util.Objects.requireNonNull;

public record ProjectRawMaterialCommand(WarehouseId warehouseId, RawMaterial rawMaterial) {
    public ProjectRawMaterialCommand {
        requireNonNull(warehouseId);
        requireNonNull(rawMaterial);
    }
}
