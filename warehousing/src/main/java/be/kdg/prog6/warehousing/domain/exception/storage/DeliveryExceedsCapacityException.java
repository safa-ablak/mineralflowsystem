package be.kdg.prog6.warehousing.domain.exception.storage;

import be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;

import java.math.BigDecimal;

public class DeliveryExceedsCapacityException extends WarehousingDomainException {
    public DeliveryExceedsCapacityException(
        final WarehouseId warehouseId,
        final BigDecimal amountToDeliver,
        final BigDecimal maxOverflowCapacity
    ) {
        super(
            "Warehouse %s cannot accept a delivery of %.2f tons. This would surpass the safe overflow capacity of %.2f tons."
                .formatted(warehouseId.id(), amountToDeliver, maxOverflowCapacity)
        );
    }
}
