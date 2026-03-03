package be.kdg.prog6.warehousing.domain.storage;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record PayloadDeliveryTicketId(UUID id) {
    public PayloadDeliveryTicketId {
        requireNonNull(id, "PayloadDeliveryTicketId cannot be null");
    }

    public static PayloadDeliveryTicketId of(final UUID id) {
        return new PayloadDeliveryTicketId(id);
    }
}
