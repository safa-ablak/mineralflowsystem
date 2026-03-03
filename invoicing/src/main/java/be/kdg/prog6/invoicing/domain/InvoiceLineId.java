package be.kdg.prog6.invoicing.domain;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record InvoiceLineId(UUID id) {
    public InvoiceLineId {
        requireNonNull(id, "InvoiceLineId cannot be null");
    }

    public static InvoiceLineId of(final UUID id) {
        return new InvoiceLineId(id);
    }

    public static InvoiceLineId newId() {
        return new InvoiceLineId(UUID.randomUUID());
    }
}
