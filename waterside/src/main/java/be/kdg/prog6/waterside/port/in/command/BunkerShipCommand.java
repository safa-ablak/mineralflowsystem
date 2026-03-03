package be.kdg.prog6.waterside.port.in.command;

import be.kdg.prog6.common.security.UserRole;
import be.kdg.prog6.waterside.domain.ShippingOrderId;

import static java.util.Objects.requireNonNull;

public record BunkerShipCommand(
    ShippingOrderId shippingOrderId,
    UserRole userRole
) {
    public BunkerShipCommand {
        requireNonNull(shippingOrderId);
        requireNonNull(userRole);
    }
}