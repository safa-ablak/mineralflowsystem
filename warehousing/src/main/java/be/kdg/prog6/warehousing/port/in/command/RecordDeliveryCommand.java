package be.kdg.prog6.warehousing.port.in.command;

import be.kdg.prog6.common.exception.InvalidOperationException;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

public record RecordDeliveryCommand(
    WarehouseId warehouseId,
    BigDecimal deliveryAmount
) {
    public RecordDeliveryCommand {
        requireNonNull(warehouseId);
        requireNonNull(deliveryAmount);
        if (deliveryAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("Amount to be delivered must be greater than 0.");
        }
    }
}
