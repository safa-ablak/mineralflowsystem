package be.kdg.prog6.warehousing.port.in.usecase.query.readmodel;

import be.kdg.prog6.warehousing.domain.storage.WarehouseId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record NetBalanceChange(
    WarehouseId warehouseId,
    LocalDateTime from,
    LocalDateTime to,
    BigDecimal netBalanceChange
) {
}