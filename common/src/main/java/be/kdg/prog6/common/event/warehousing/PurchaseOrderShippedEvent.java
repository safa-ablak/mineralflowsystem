package be.kdg.prog6.common.event.warehousing;

import java.util.UUID;

public record PurchaseOrderShippedEvent(UUID purchaseOrderId) {
}
