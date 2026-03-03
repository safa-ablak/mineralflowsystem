package be.kdg.prog6.waterside.port.in.usecase;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.port.in.command.InspectShipCommand;

@FunctionalInterface
public interface InspectShipUseCase {
    ShippingOrder inspectShip(InspectShipCommand command);
}
