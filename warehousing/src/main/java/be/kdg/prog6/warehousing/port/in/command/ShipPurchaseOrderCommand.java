package be.kdg.prog6.warehousing.port.in.command;

import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;

import static java.util.Objects.requireNonNull;

// For a PO, ship from relevant warehouse(s)
public record ShipPurchaseOrderCommand(PurchaseOrderId purchaseOrderId) {
    public ShipPurchaseOrderCommand {
        requireNonNull(purchaseOrderId);
    }
}
