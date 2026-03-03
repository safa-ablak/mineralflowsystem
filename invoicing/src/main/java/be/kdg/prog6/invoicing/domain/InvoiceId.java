package be.kdg.prog6.invoicing.domain;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record InvoiceId(UUID id) {
    public InvoiceId {
        requireNonNull(id, "InvoiceId cannot be null");
    }

    public static InvoiceId of(final UUID id) {
        return new InvoiceId(id);
    }

    public static InvoiceId newId() {
        return new InvoiceId(UUID.randomUUID());
    }
}
