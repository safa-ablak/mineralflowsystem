package be.kdg.prog6.warehousing.adapter.in.web.dto;

import be.kdg.prog6.warehousing.domain.storage.Balance;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BalanceDto(LocalDateTime time, BigDecimal amount) {
    public static BalanceDto of(final Balance balance) {
        return new BalanceDto(balance.time(), balance.amount());
    }
}