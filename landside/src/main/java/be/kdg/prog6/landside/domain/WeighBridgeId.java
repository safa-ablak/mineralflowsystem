package be.kdg.prog6.landside.domain;

import java.util.UUID;

public record WeighBridgeId(UUID id) {
    public static WeighBridgeId of(final UUID id) {
        return new WeighBridgeId(id);
    }

    public static WeighBridgeId newId() {
        return new WeighBridgeId(UUID.randomUUID());
    }
}