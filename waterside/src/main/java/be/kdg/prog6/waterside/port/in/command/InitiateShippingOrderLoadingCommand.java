package be.kdg.prog6.waterside.port.in.command;

import be.kdg.prog6.waterside.domain.ShippingOrderId;

import static java.util.Objects.requireNonNull;

public record InitiateShippingOrderLoadingCommand(ShippingOrderId shippingOrderId) {
    public InitiateShippingOrderLoadingCommand {
        requireNonNull(shippingOrderId);
    }
}
