package be.kdg.prog6.warehousing.port.in.command;

import be.kdg.prog6.warehousing.domain.storage.RawMaterial;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;

import static java.util.Objects.requireNonNull;

public record AssignRawMaterialToWarehouseCommand(
    WarehouseId warehouseId,
    RawMaterial rawMaterial
) {
    public AssignRawMaterialToWarehouseCommand {
        requireNonNull(warehouseId);
        requireNonNull(rawMaterial);
    }
}
