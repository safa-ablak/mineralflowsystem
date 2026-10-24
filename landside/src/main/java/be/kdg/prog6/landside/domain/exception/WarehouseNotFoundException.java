package be.kdg.prog6.landside.domain.exception;

import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.landside.domain.RawMaterial;
import be.kdg.prog6.landside.domain.SupplierId;

public class WarehouseNotFoundException extends NotFoundException {
    public WarehouseNotFoundException(final SupplierId supplierId, final RawMaterial rawMaterial) {
        super(String.format(
            "Warehouse with Supplier ID %s and Raw Material %s not found", supplierId.id(), rawMaterial
        ));
    }
}
