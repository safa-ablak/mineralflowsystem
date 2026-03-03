package be.kdg.prog6.waterside.port.in.usecase;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.port.in.command.BunkerShipCommand;

@FunctionalInterface
public interface BunkerShipUseCase {
    ShippingOrder bunkerShip(BunkerShipCommand command);
}