package be.kdg.prog6.warehousing.domain.exception.storage;

import be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;

import java.math.BigDecimal;

public class InsufficientStockException extends WarehousingDomainException {
    public InsufficientStockException(
        final WarehouseId warehouseId,
        final BigDecimal amountToShip,
        final BigDecimal shortfall
    ) {
        super(
            "Warehouse %s cannot process a shipment of %.2f tons. Insufficient stock. Shortfall: %.2f tons."
                .formatted(warehouseId.id(), amountToShip, shortfall)
        );
    }
}
