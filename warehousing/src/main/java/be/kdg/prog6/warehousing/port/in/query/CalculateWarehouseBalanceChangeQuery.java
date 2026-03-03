package be.kdg.prog6.warehousing.port.in.query;

import be.kdg.prog6.common.exception.InvalidOperationException;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

public record CalculateWarehouseBalanceChangeQuery(
    WarehouseId warehouseId,
    LocalDateTime from,
    LocalDateTime to
) {
    public CalculateWarehouseBalanceChangeQuery {
        requireNonNull(warehouseId);
        requireNonNull(from);
        requireNonNull(to);
        if (from.isAfter(to)) {
            throw new InvalidOperationException("The '%s' date must be before or equal to the '%s' date"
                .formatted(from, to)
            );
        }
    }
}
