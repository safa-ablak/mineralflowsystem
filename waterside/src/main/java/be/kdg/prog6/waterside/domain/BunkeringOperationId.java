package be.kdg.prog6.waterside.domain;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

// We don't need to refer to BOs via their own ID, but I just have it anyway (similar to OrderLine in the Warehousing Ctx)
public record BunkeringOperationId(UUID id) {
    public BunkeringOperationId {
        requireNonNull(id, "BunkeringOperationId cannot be null");
    }

    public static BunkeringOperationId of(final UUID id) {
        return new BunkeringOperationId(id);
    }
}
