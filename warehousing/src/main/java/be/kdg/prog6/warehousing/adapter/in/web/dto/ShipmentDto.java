package be.kdg.prog6.warehousing.adapter.in.web.dto;

import be.kdg.prog6.warehousing.domain.storage.Shipment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShipmentDto(String id, LocalDateTime time, BigDecimal amount) {
    public static ShipmentDto fromDomain(final Shipment shipment) {
        return new ShipmentDto(
            shipment.id().id().toString(),
            shipment.time(),
            shipment.amount()
        );
    }
}