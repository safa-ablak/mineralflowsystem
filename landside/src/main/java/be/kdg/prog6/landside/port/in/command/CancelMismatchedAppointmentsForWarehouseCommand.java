package be.kdg.prog6.landside.port.in.command;

import be.kdg.prog6.landside.domain.RawMaterial;
import be.kdg.prog6.landside.domain.WarehouseId;

import static java.util.Objects.requireNonNull;

public record CancelMismatchedAppointmentsForWarehouseCommand(WarehouseId warehouseId, RawMaterial newRawMaterial) {
    public CancelMismatchedAppointmentsForWarehouseCommand {
        requireNonNull(warehouseId);
        requireNonNull(newRawMaterial);
    }
}
