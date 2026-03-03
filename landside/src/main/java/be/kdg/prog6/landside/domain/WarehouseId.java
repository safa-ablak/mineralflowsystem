package be.kdg.prog6.landside.domain;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record WarehouseId(UUID id) {
    public WarehouseId {
        requireNonNull(id, "WarehouseId cannot be null");
    }

    public static WarehouseId of(final UUID id) {
        return new WarehouseId(id);
    }
}
