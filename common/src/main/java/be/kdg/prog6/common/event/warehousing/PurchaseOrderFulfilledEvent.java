package be.kdg.prog6.common.event.warehousing;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record PurchaseOrderFulfilledEvent(
    UUID sellerId,
    // A map for the raw material amounts of the PO: <key = raw material, value = amount>
    Map<String, BigDecimal> rawMaterialToAmount
) {
}