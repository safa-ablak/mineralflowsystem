package be.kdg.prog6.warehousing.domain.exception.storage;

import be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;

import java.math.BigDecimal;

public class DeliveryCapacityExceededException extends WarehousingDomainException {
    private DeliveryCapacityExceededException(final String message) {
        super(message);
    }

    public static DeliveryCapacityExceededException forDelivery(
        final WarehouseId warehouseId,
        final BigDecimal amountToDeliver,
        final BigDecimal maxOverflowCapacity
    ) {
        return new DeliveryCapacityExceededException(
            "Warehouse %s cannot accept a delivery of %.2f tons. This would surpass the safe overflow capacity of %.2f tons."
                .formatted(warehouseId.id(), amountToDeliver, maxOverflowCapacity)
        );
    }
}
