package be.kdg.prog6.warehousing.domain.exception.storage;

import be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException;
import be.kdg.prog6.warehousing.domain.storage.RawMaterial;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;

public class RawMaterialConflictException extends WarehousingDomainException {
    public RawMaterialConflictException(final WarehouseId warehouseId,
                                        final RawMaterial current,
                                        final RawMaterial attempted) {
        super("Warehouse %s already stores %s and is not empty. Cannot assign %s.".formatted(
            warehouseId.id(), current, attempted
        ));
    }
}
