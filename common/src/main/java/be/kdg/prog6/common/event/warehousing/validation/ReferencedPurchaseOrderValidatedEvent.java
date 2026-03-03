package be.kdg.prog6.common.event.warehousing.validation;

import java.util.UUID;

/// This event will be emitted if the referenced purchase order ID is valid for a shipping order
public record ReferencedPurchaseOrderValidatedEvent(UUID purchaseOrderId) {
}
