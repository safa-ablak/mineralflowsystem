package be.kdg.prog6.warehousing.domain;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record SellerId(UUID id) {
    public SellerId {
        requireNonNull(id, "SellerId cannot be null");
    }

    public static SellerId of(final UUID id) {
        return new SellerId(id);
    }

    public static SellerId newId() {
        return new SellerId(UUID.randomUUID());
    }
}
