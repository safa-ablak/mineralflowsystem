package be.kdg.prog6.waterside.port.in.command;

import be.kdg.prog6.waterside.domain.ReferenceId;

import static java.util.Objects.requireNonNull;

/**
 * Represents a command to match a Shipping Order with a Purchase Order.
 *
 * @param referenceId The unique identifier of referenced Purchase Order.
 */
public record MatchShippingOrderWithPurchaseOrderCommand(ReferenceId referenceId) {
    public MatchShippingOrderWithPurchaseOrderCommand {
        requireNonNull(referenceId);
    }
}
