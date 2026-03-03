package be.kdg.prog6.waterside.port.in.command;

import be.kdg.prog6.common.exception.InvalidOperationException;
import be.kdg.prog6.waterside.domain.BuyerId;
import be.kdg.prog6.waterside.domain.ReferenceId;
import be.kdg.prog6.waterside.domain.ShippingOrderId;

import static java.util.Objects.requireNonNull;

public record RequestShippingOrderCorrectionCommand(
    ShippingOrderId shippingOrderId,
    BuyerId buyerId,
    ReferenceId referenceId
) {
    public RequestShippingOrderCorrectionCommand {
        requireNonNull(shippingOrderId);
        if (noCorrectionFieldsProvided(buyerId, referenceId)) {
            throw new InvalidOperationException(
                "At least one of Buyer ID or Reference ID must be provided"
            );
        }
    }

    /**
     * A SO correction must provide at least one piece of identifying information.
     * Either Buyer ID or Reference ID (or both) must be present; they cannot both be null.
     */
    private static boolean noCorrectionFieldsProvided(final BuyerId buyerId, final ReferenceId referenceId) {
        return buyerId == null && referenceId == null;
    }
}