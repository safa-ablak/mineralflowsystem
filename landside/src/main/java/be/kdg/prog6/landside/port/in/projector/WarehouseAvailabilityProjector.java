package be.kdg.prog6.landside.port.in.projector;

import be.kdg.prog6.landside.port.in.command.ProjectAvailabilityCommand;

@FunctionalInterface
public interface WarehouseAvailabilityProjector {
    void projectAvailability(ProjectAvailabilityCommand command);
}
