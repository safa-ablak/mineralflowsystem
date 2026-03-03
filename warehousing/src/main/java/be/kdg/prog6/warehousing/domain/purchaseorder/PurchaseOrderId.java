package be.kdg.prog6.warehousing.domain.purchaseorder;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record PurchaseOrderId(UUID id) {
    public PurchaseOrderId {
        requireNonNull(id, "PurchaseOrderId cannot be null");
    }

    public static PurchaseOrderId of(final UUID id) {
        return new PurchaseOrderId(id);
    }

    public static PurchaseOrderId newId() {
        return new PurchaseOrderId(UUID.randomUUID());
    }
}
