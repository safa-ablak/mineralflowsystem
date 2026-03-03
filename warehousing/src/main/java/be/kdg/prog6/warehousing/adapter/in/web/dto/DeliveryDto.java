package be.kdg.prog6.warehousing.adapter.in.web.dto;

import be.kdg.prog6.warehousing.domain.storage.Delivery;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DeliveryDto(String id, LocalDateTime time, BigDecimal amount) {
    public static DeliveryDto fromDomain(final Delivery delivery) {
        return new DeliveryDto(
            delivery.id().id().toString(),
            delivery.time(),
            delivery.amount()
        );
    }
}