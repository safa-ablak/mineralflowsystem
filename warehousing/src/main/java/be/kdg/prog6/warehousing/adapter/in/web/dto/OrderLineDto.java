package be.kdg.prog6.warehousing.adapter.in.web.dto;

import be.kdg.prog6.warehousing.domain.purchaseorder.OrderLine;

import java.math.BigDecimal;

public record OrderLineDto(
    int lineNumber,
    String rawMaterial,
    BigDecimal amount
) {
    public static OrderLineDto fromDomain(final OrderLine orderLine) {
        return new OrderLineDto(
            orderLine.getLineNumber(),
            orderLine.getRawMaterial().name(),
            orderLine.getAmount()
        );
    }
}
