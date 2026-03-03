package be.kdg.prog6.warehousing.domain.exception.storage;

import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;

public class WarehouseNotFoundException extends NotFoundException {
    private WarehouseNotFoundException(String message) {
        super(message);
    }

    public static WarehouseNotFoundException forId(final WarehouseId id) {
        return new WarehouseNotFoundException(
            String.format("Warehouse with ID %s not found", id.id())
        );
    }
}
