package be.kdg.prog6.waterside.domain;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record ReferenceId(UUID id) { // PO reference
    public ReferenceId {
        requireNonNull(id, "ReferenceId cannot be null");
    }

    public static ReferenceId of(final UUID id) {
        return new ReferenceId(id);
    }

    public static ReferenceId newId() {
        return new ReferenceId(UUID.randomUUID());
    }

    public static ReferenceId ofNullable(final UUID id) {
        return id != null ? new ReferenceId(id) : null;
    }
}
