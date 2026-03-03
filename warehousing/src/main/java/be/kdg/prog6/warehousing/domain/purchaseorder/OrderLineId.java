package be.kdg.prog6.warehousing.domain.purchaseorder;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record OrderLineId(UUID id) {
    public OrderLineId {
        requireNonNull(id, "OrderLineId cannot be null");
    }

    public static OrderLineId of(final UUID id) {
        return new OrderLineId(id);
    }

    public static OrderLineId newId() {
        return new OrderLineId(UUID.randomUUID());
    }
}
