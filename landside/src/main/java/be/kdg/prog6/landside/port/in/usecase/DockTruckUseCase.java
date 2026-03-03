package be.kdg.prog6.landside.port.in.usecase;

import be.kdg.prog6.landside.port.in.command.DockTruckCommand;
import be.kdg.prog6.landside.port.in.usecase.model.DockedTruck;

@FunctionalInterface
public interface DockTruckUseCase {
    DockedTruck dockTruck(DockTruckCommand command);
}
