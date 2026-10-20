package be.kdg.prog6.warehousing.domain.exception.storage;

import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;

public class WarehouseNotFoundException extends NotFoundException {
    public WarehouseNotFoundException(final WarehouseId id) {
        super(
            String.format("Warehouse with ID %s not found", id.id())
        );
    }
}
