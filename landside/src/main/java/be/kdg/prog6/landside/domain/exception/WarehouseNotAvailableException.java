package be.kdg.prog6.landside.domain.exception;

import be.kdg.prog6.landside.domain.RawMaterial;
import be.kdg.prog6.landside.domain.SupplierId;
import be.kdg.prog6.landside.domain.WarehouseId;

public class WarehouseNotAvailableException extends LandsideDomainException {
    private WarehouseNotAvailableException(String message) {
        super(message);
    }

    public static WarehouseNotAvailableException forNewAppointment(
        final WarehouseId warehouseId,
        final SupplierId supplierId,
        final RawMaterial rawMaterial
    ) {
        return new WarehouseNotAvailableException(
            String.format(
                "Warehouse %s for Supplier %s (Raw Material %s) is not available for new Appointments",
                warehouseId.id(),
                supplierId.id(),
                rawMaterial
            )
        );
    }
}
