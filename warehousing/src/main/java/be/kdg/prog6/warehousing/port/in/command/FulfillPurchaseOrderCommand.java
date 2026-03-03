package be.kdg.prog6.warehousing.port.in.command;

import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;

import static java.util.Objects.requireNonNull;

public record FulfillPurchaseOrderCommand(PurchaseOrderId purchaseOrderId) {
    public FulfillPurchaseOrderCommand {
        requireNonNull(purchaseOrderId);
    }
}
