package be.kdg.prog6.waterside.domain;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record BuyerId(UUID id) {
    public BuyerId {
        requireNonNull(id, "BuyerId cannot be null");
    }

    public static BuyerId of(final UUID id) {
        return new BuyerId(id);
    }

    public static BuyerId ofNullable(final UUID id) {
        return id != null ? new BuyerId(id) : null;
    }
}
