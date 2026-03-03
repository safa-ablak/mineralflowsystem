package be.kdg.prog6.landside.domain;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

// Context Mapping:
// "Sellers" are responsible for supplying the raw materials in the Landside BC.
// They are not selling anything in the landside, that's happening in the Warehousing BC.
// Hence, the name "Supplier" instead of "Seller".
public record SupplierId(UUID id) {
    public SupplierId {
        requireNonNull(id, "SupplierId cannot be null");
    }

    public static SupplierId of(final UUID id) {
        return new SupplierId(id);
    }
}