package be.kdg.prog6.waterside.port.in.command;

import be.kdg.prog6.common.exception.InvalidOperationException;
import be.kdg.prog6.waterside.domain.ShippingOrderId;

import static java.util.Objects.requireNonNull;

public record InspectShipCommand(
    ShippingOrderId shippingOrderId,
    String inspectorSignature
) {
    public InspectShipCommand {
        requireNonNull(shippingOrderId);
        requireNonNull(inspectorSignature);
        if (inspectorSignature.isBlank()) {
            throw new InvalidOperationException("Inspector signature must not be blank");
        }
    }
}
