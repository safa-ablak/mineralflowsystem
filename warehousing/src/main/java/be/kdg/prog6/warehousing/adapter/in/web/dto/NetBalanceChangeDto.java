package be.kdg.prog6.warehousing.adapter.in.web.dto;

import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.NetBalanceChange;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record NetBalanceChangeDto(
    String warehouseId,
    LocalDateTime from,
    LocalDateTime to,
    BigDecimal netBalanceChange
) {
    public static NetBalanceChangeDto of(final NetBalanceChange netBalanceChange) {
        return new NetBalanceChangeDto(
            netBalanceChange.warehouseId().id().toString(),
            netBalanceChange.from(),
            netBalanceChange.to(),
            netBalanceChange.netBalanceChange()
        );
    }
}
