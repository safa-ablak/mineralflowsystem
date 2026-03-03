package be.kdg.prog6.landside.domain;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record VisitId(UUID id) {
    public VisitId {
        requireNonNull(id, "VisitId cannot be null");
    }

    public static VisitId of(final UUID id) {
        return new VisitId(id);
    }

    public static VisitId newId() {
        return new VisitId(UUID.randomUUID());
    }
}
