package be.kdg.prog6.invoicing.adapter.in.web.dto;

import be.kdg.prog6.invoicing.domain.Money;

import java.math.BigDecimal;

public record MoneyDto(BigDecimal amount, String currencyCode) {
    public static MoneyDto of(final Money money) {
        return new MoneyDto(money.getAmount(), money.getCurrency().getCurrencyCode());
    }
}
