package be.kdg.prog6.warehousing.port.in.command;

import be.kdg.prog6.warehousing.domain.BuyerId;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record ValidateReferencedPurchaseOrderCommand(
    BuyerId buyerId,
    PurchaseOrderId purchaseOrderId,
    UUID shippingOrderId,
    String vesselNumber
) {
    public ValidateReferencedPurchaseOrderCommand {
        requireNonNull(buyerId);
        requireNonNull(purchaseOrderId);
        requireNonNull(shippingOrderId);
    }
}