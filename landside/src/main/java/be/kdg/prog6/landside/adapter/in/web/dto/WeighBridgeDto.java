package be.kdg.prog6.landside.adapter.in.web.dto;

import be.kdg.prog6.landside.domain.WeighBridge;

import java.util.UUID;

public record WeighBridgeDto(
    UUID id,
    String number,
    UUID occupiedByVisitId
) {
    public static WeighBridgeDto fromDomain(final WeighBridge weighBridge) {
        final boolean isOccupied = weighBridge.isOccupied();
        return new WeighBridgeDto(
            weighBridge.getId().id(),
            weighBridge.getNumber().value(),
            isOccupied ? weighBridge.getOccupiedByVisitId().id() : null
        );
    }
}