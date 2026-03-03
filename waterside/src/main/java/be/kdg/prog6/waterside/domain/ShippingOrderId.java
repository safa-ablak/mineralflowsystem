package be.kdg.prog6.waterside.domain;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record ShippingOrderId(UUID id) {
    public ShippingOrderId {
        requireNonNull(id, "ShippingOrderId cannot be null");
    }

    public static ShippingOrderId newId() {
        return new ShippingOrderId(UUID.randomUUID());
    }

    public static ShippingOrderId of(final UUID id) {
        return new ShippingOrderId(id);
    }
}
