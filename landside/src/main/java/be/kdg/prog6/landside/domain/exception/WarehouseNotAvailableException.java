package be.kdg.prog6.landside.domain.exception;

import be.kdg.prog6.landside.domain.RawMaterial;
import be.kdg.prog6.landside.domain.SupplierId;
import be.kdg.prog6.landside.domain.WarehouseId;

public class WarehouseNotAvailableException extends LandsideDomainException {
    public WarehouseNotAvailableException(
        final WarehouseId warehouseId,
        final SupplierId supplierId,
        final RawMaterial rawMaterial
    ) {
        super(String.format(
            "Warehouse %s for Supplier %s (Raw Material %s) is not available for new Appointments",
            warehouseId.id(),
            supplierId.id(),
            rawMaterial
        ));
    }
}
