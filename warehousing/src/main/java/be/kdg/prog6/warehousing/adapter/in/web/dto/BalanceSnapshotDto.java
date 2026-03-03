package be.kdg.prog6.warehousing.adapter.in.web.dto;

import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.BalanceSnapshot;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BalanceSnapshotDto(String warehouseId, LocalDateTime snapshotTime, BigDecimal amount) {
    public static BalanceSnapshotDto of(final BalanceSnapshot snapshot) {
        return new BalanceSnapshotDto(
            snapshot.warehouseId().id().toString(),
            snapshot.snapshotTime(),
            snapshot.amount()
        );
    }
}
