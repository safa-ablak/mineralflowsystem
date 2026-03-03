package be.kdg.prog6.invoicing.domain;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record RawMaterialId(UUID id) {
    public RawMaterialId {
        requireNonNull(id, "RawMaterialId cannot be null");
    }

    public static RawMaterialId of(final UUID id) {
        return new RawMaterialId(id);
    }

    public static RawMaterialId newId() {
        return new RawMaterialId(UUID.randomUUID());
    }
}
