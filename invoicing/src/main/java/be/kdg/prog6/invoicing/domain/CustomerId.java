package be.kdg.prog6.invoicing.domain;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

// Context Mapping: "Sellers" are our Customers in the Invoicing BC
public record CustomerId(UUID id) {
    public CustomerId {
        requireNonNull(id, "CustomerId cannot be null");
    }

    public static CustomerId of(final UUID id) {
        return new CustomerId(id);
    }
}
