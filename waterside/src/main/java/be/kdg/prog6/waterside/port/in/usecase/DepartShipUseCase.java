package be.kdg.prog6.waterside.port.in.usecase;

import be.kdg.prog6.waterside.port.in.command.DepartShipCommand;

@FunctionalInterface
public interface DepartShipUseCase {
    void departShip(DepartShipCommand command);
}
