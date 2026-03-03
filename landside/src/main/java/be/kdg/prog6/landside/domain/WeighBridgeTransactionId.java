package be.kdg.prog6.landside.domain;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record WeighBridgeTransactionId(UUID id) {
    public WeighBridgeTransactionId {
        requireNonNull(id, "WeighBridgeTransactionId cannot be null");
    }

    public static WeighBridgeTransactionId of(final UUID id) {
        return new WeighBridgeTransactionId(id);
    }
}
