package be.kdg.prog6.waterside.port.in.usecase;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.port.in.command.DockShipCommand;

@FunctionalInterface
public interface DockShipUseCase {
    // Dock the ship at the port -> Fill in the actual arrival date.
    ShippingOrder dockShip(DockShipCommand command);
}
