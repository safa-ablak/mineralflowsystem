package be.kdg.prog6.common.event.landside;

import java.math.BigDecimal;
import java.util.UUID;

public record TruckWeighedOutEvent(
    UUID warehouseId,
    BigDecimal netWeight
) {
}
