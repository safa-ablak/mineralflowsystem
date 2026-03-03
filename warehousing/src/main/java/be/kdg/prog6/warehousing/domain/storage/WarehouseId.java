package be.kdg.prog6.warehousing.domain.storage;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record WarehouseId(UUID id) {
    public WarehouseId {
        requireNonNull(id, "WarehouseId cannot be null");
    }

    public static WarehouseId of(final UUID id) {
        return new WarehouseId(id);
    }

    public static WarehouseId newId() {
        return new WarehouseId(UUID.randomUUID());
    }
}
