package be.kdg.prog6.warehousing.port.in.usecase.query.readmodel;

import be.kdg.prog6.warehousing.domain.storage.WarehouseId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BalanceSnapshot(
    WarehouseId warehouseId,
    LocalDateTime snapshotTime,
    BigDecimal amount
) {
    /**
     * Creates an origin snapshot for a warehouse with zero balance at the earliest possible time.
     * This represents the initial state before any deliveries or shipments.
     *
     * @param warehouseId the warehouse to create an origin snapshot for
     * @return a new balance snapshot with zero balance at {@link LocalDateTime#MIN}
     */
    public static BalanceSnapshot origin(final WarehouseId warehouseId) {
        return new BalanceSnapshot(warehouseId, LocalDateTime.MIN, BigDecimal.ZERO);
    }
}
