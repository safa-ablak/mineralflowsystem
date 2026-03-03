package be.kdg.prog6.warehousing.port.in.query;

import be.kdg.prog6.warehousing.domain.storage.WarehouseId;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

public record GetWarehouseBalanceQuery(
    WarehouseId warehouseId,
    LocalDateTime asOf
) {
    public GetWarehouseBalanceQuery {
        requireNonNull(warehouseId);
        requireNonNull(asOf);
    }
}
